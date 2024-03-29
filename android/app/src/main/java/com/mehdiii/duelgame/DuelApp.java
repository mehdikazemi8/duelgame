package com.mehdiii.duelgame;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.mehdiii.duelgame.api.ChatApi;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.PurchaseManager;
import com.mehdiii.duelgame.models.SyncData;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.chat.DaoMaster;
import com.mehdiii.duelgame.models.chat.DaoSession;
import com.mehdiii.duelgame.models.chat.MessageDao;
import com.mehdiii.duelgame.models.events.OnConnectionStateChanged;
import com.mehdiii.duelgame.models.events.OnSyncDataReceived;
import com.mehdiii.duelgame.utils.CleanDB;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.splunk.mint.Mint;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class DuelApp extends Application implements Application.ActivityLifecycleCallbacks {
    public static final String PROPERTY_ID = "UA-62041991-1";
    public static final String TAG = "DUEL_APP";
    private static DuelApp instance;
    static protected WebSocketConnection wsc = new WebSocketConnection();
    Map<Integer, BaseModel> pendingMessages = new HashMap<>();

    public static String BASE_URL = "http://duelgame.ir:8000";
//    public static String BASE_URL = "http://192.168.88.44:8000";

    static protected String wsuri = "ws://duelgame.ir:9003";
//    static protected String wsuri = "ws://192.168.44.23:9000";
//    static protected String wsuri = "ws://192.168.177.153:9000";
//    static protected String wsuri = "ws://192.168.88.44:9000";

    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "a]ppVersion";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Mint.initAndStartSession(this, "fe0c054c");

        resumedActivities = 0;

        initGA();
        if (!wsc.isConnected())
            connectToWs();

        PurchaseManager.init(this);

        retrofit = getRetrofit();
        configureORM();

        new CleanDB().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public boolean isConnected() {
        return wsc.isConnected();
    }

    public void connectToWs() {
        try {
            EventBus.getDefault().post(new OnConnectionStateChanged(OnConnectionStateChanged.ConnectionState.CONNECTING));
            wsc.connect(wsuri, new WebSocketHandler() {
                @Override
                public void onOpen() {
                    Log.d(TAG, "Status: Connected to " + wsuri);
                    EventBus.getDefault().post(new OnConnectionStateChanged(OnConnectionStateChanged.ConnectionState.CONNECTED));
                }

                @Override
                public void onTextMessage(String payload) {
                    Log.d(TAG, "Got echo: " + payload);
                    dispatchMessage(payload);
                }

                @Override
                public void onClose(int code, String reason) {
                    EventBus.getDefault().post(new OnConnectionStateChanged(OnConnectionStateChanged.ConnectionState.DISCONNECTED));
                    Log.d(TAG, "Connection lost." + reason);
                }
            });
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (wsc.isConnected())
            wsc.disconnect();
    }

    private void syncData(String json) {
        BaseModel baseModel = BaseModel.deserialize(json, BaseModel.class);
        if (baseModel != null &&
                baseModel.getCommand() != null &&
                baseModel.getCommand() == CommandType.RECEIVE_SYNC_DATA) {

            SyncData syncData = SyncData.deserialize(json, SyncData.class);
            if (AuthManager.getCurrentUser() != null && syncData != null && syncData.getDiamond() != null) {
                AuthManager.getCurrentUser().setDiamond(syncData.getDiamond());
                AuthManager.getCurrentUser().setHeart(syncData.getHeart());
                AuthManager.getCurrentUser().setPendingOfflineChallenges(syncData.getPendingOfflineChallenges());

                AuthManager.getCurrentUser().setScores(syncData.getScores());
                AuthManager.getCurrentUser().setWeeklyRanks(syncData.getWeeklyRanks());

                AuthManager.getCurrentUser().setFreeExamCount(syncData.getFreeExamCount());
                AuthManager.getCurrentUser().setSubscribedForExam(syncData.isSubscribedForExam());
                AuthManager.getCurrentUser().setInvitedByMe(syncData.getInvitedByMe());
            }
        }

        EventBus.getDefault().post(new OnSyncDataReceived());
    }

    /**
     * sends off messages delivered to wsc from server to all of the app.
     *
     * @param json the message received from server
     */
    public void dispatchMessage(String json) {
        Intent i = new Intent(DuelBroadcastReceiver.ACTION_NAME);
        i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        i.putExtra(DuelBroadcastReceiver.BUNDLE_JSON_KEY, json);

        Log.d("TAG", "dispatchMessage");

        // use `local broadcast manager` instead of `global broadcast manager` to avoid unnecessary calls to other apps
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);

        syncData(json);
    }

    public IntentFilter getIntentFilter() {
        return new IntentFilter(DuelBroadcastReceiver.ACTION_NAME);
    }

    public void sendMessage(String json) {
        try {
            Log.d("sentit", json);
            if (wsc == null) {
                Log.e("sentit", "wsc is null");
                return;
            }
            if (!wsc.isConnected()) {
                Log.e("sentit", "wsc is not connected");
                return;
            }
            wsc.sendTextMessage(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * used to send a message and receive delivery message so that you can check whether request is
     * reached/processed successfully on server or not?
     *
     * @param model                     the model that is used to be sent off to the server
     * @param onMessageReceivedListener this listens for delivery message of current request and calls onDelivered
     *                                  when delivery message is received.
     */
    public void sendMessageWithDelivery(BaseModel model, OnMessageReceivedListener onMessageReceivedListener) {
        pendingMessages.put(model.hashCode(), model);
        sendMessage(model.serialize());
    }

    public WebSocketConnection getSocket() {
        return wsc;
    }

    public static synchronized DuelApp getInstance() {
        return instance;
    }

    /**
     * used as tell android display a global toast messages in screen.
     *
     * @param resourceId reference to the string resource in which toast is going to display
     * @param length     length of the toast, i.e. typically TOAST.LENGTH_SHORT or TOAST.LENGTH_LONG
     */
    public void toast(int resourceId, int length) {
        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(resourceId), length);
        toast.setGravity(Gravity.TOP, 0, 180);
        toast.show();
    }

    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     * <p/>
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them all in Application object helps ensure that they are created only once per
     * application instance.
     */
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : analytics.newTracker(R.xml.global_tracker);

            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }

    private void initGA() {
        Tracker mGATracker = getTracker(TrackerName.GLOBAL_TRACKER);
        mGATracker.setSessionTimeout(300);
        mGATracker.enableAutoActivityTracking(true);
        mGATracker.enableExceptionReporting(true);
    }


    /**
     * Gets the current registration ID for application on GCM service.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    public static String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId == null || registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId   registration ID
     */
    public static void storeRegistrationId(Context context, String regId) {

        final SharedPreferences prefs = DuelApp.getGCMPreferences(context);
        int appVersion = DuelApp.getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);

        // persist preference
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    public static SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return context.getSharedPreferences(DuelApp.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }


    /**
     * Implementing ActivityLifecycleCallbacks
     */

    private int resumedActivities;

    public int getResumedActivities() {
        return resumedActivities;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.d("TAG", "ActivityLifecycle onActivityStarted");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d("TAG", "ActivityLifecycle onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        Log.d("TAG", "ActivityLifecycle onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.d("TAG", "ActivityLifecycle onActivityDestroyed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        resumedActivities--;
        Log.d("TAG", "ActivityLifecycle onActivityPaused " + getResumedActivities() + " " + Calendar.getInstance().getTime().toString());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        resumedActivities++;
        Log.d("TAG", "ActivityLifecycle onActivityResumed " + getResumedActivities() + " " + Calendar.getInstance().getTime().toString());
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        Log.d("TAG", "ActivityLifecycle onActivityCreated");
    }


    // Chat section
    public static final String DB_NAME = "duel_db";
    private static Retrofit retrofit;
    public static SQLiteDatabase db;
    public static DaoMaster daoMaster;
    public static DaoSession daoSession;
    public static Cursor cursor;
    public static MessageDao messageDao;

    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public static ChatApi createChatApi() {
        return retrofit.create(ChatApi.class);
    }

    private void configureORM() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, DB_NAME, null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        messageDao = daoSession.getMessageDao();
    }
}
