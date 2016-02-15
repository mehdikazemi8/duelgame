package com.mehdiii.duelgame.views.activities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.PurchaseManager;
import com.mehdiii.duelgame.models.BuyNotification;
import com.mehdiii.duelgame.models.ChallengeRequestDecision;
import com.mehdiii.duelgame.models.DuelOpponentRequest;
import com.mehdiii.duelgame.models.LoginRequest;
import com.mehdiii.duelgame.models.Question;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.events.OnConnectionStateChanged;
import com.mehdiii.duelgame.models.events.OnPurchaseResult;
import com.mehdiii.duelgame.receivers.DuelHourStarted;
import com.mehdiii.duelgame.utils.DeviceManager;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.waiting.WaitingActivity;
import com.mehdiii.duelgame.views.dialogs.AlertDialog;
import com.mehdiii.duelgame.views.dialogs.AnswerDuelWithFriendRequestDialog;
import com.mehdiii.duelgame.views.dialogs.ConfirmDialog;
import com.mehdiii.duelgame.views.dialogs.ConnectionLostDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import de.greenrobot.event.EventBus;

public class ParentActivity extends ActionBarActivity {

    protected BroadcastReceiver receiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (type == CommandType.RECEIVE_LOGIN_INFO) {
                User user = BaseModel.deserialize(json, User.class);
                if (user != null) {
                    AuthManager.authenticate(ParentActivity.this, user);
                }
            } else if (type == CommandType.RECEIVE_CHALLENGE_REQUEST) {
                if(canHandleChallengeRequest()) {
                    DuelOpponentRequest request = BaseModel.deserialize(json, DuelOpponentRequest.class);
                    try {
                        category = String.valueOf(request.getCategory());
                        AnswerDuelWithFriendRequestDialog dialog = new AnswerDuelWithFriendRequestDialog(ParentActivity.this, request);
                        dialog.setCancelable(false);
                        dialog.onDecisionMade(onDecisionMadeListener());
                        dialog.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if(isInQuizActivity()) {
                    DuelOpponentRequest data = BaseModel.deserialize(json, DuelOpponentRequest.class);
                    ChallengeRequestDecision requestDecision = new ChallengeRequestDecision(CommandType.SEND_ANSWER_OF_CHALLENGE_REQUEST);
                    requestDecision.setDecision(6);
                    requestDecision.setUserNumber(data.getId());
                    requestDecision.setCategory(data.getCategory());
                    DuelApp.getInstance().sendMessage(requestDecision.serialize());
                }
            } else if(type == CommandType.RECEIVE_CHALLENGE_UPDATES) {
                Log.d("TAG", "update " + json);
                AlertDialog dialog = new AlertDialog(ParentActivity.this, "eaeuoaeou");
                dialog.show();
            }
        }
    });


    public static int STORE_PAGE = 10;
    public static int SETTINGS_PAGE = 11;

    public static String STORE_FRAGMENT = "STORE_FRAGMENT";
    public static String QUIZ_INFO_FRAGMENT = "quiz_info_fragment";
    public static String QUIZ_RESULT_FRAGMENT = "QUIZ_RESULT_FRAGMENT";
    public static String REVIEW_QUIZ_FRAGMENT = "REVIEW_QUIZ_FRAGMENT";
    public static String SETTINGS_FRAGMENT = "SETTINGS_FRAGMENT";
    public static String DUEL_HOUR_TOTAL_FRAGMENT = "DUEL_HOUR_TOTAL_FRAGMENT";

    public static int SEPARATOR_CUP = 404;

    public static final int DUEL_HOUR_NOTIFICATION_ID = 1708;
    public static final int DUEL_HOUR_ALARM_ID = 1709;

    private static final int DUEL_HOUR_HOUR = 20;
    private static final int DUEL_HOUR_MINUTE = 0;
    private static final int DUEL_HOUR_SECOND = 0;
    private static final long DUEL_HOUR_INTERVAL = AlarmManager.INTERVAL_DAY;
//    private static final long DUEL_HOUR_INTERVAL = 30*1000;

    protected void cancelDuelHourNotification() {
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(DUEL_HOUR_NOTIFICATION_ID);
    }

    public static boolean isItNearDuelHour() {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());
        long now = calendar.getTimeInMillis();

        calendar.set(Calendar.HOUR_OF_DAY, DUEL_HOUR_HOUR);
        calendar.set(Calendar.MINUTE, DUEL_HOUR_MINUTE);
        calendar.set(Calendar.SECOND, DUEL_HOUR_SECOND);
        long duelHour = calendar.getTimeInMillis();

        return Math.abs(now - duelHour) < 1000*60*2;
    }

    public void onEvent(BuyNotification notif) {
        switch (notif.getType()) {
            case 1:
                PurchaseManager.getInstance().startPurchase(notif.getId());
                break;
            case 2:
                PurchaseManager.getInstance().useDiamond(notif);
                break;
        }
    }

    public void onEvent(OnPurchaseResult purchaseResult) {
        if(purchaseResult == null) {
            Log.d("TAG", "alert is null");
        } else if(purchaseResult.getStatus() == null) {
            Log.d("TAG", "alert.getStatus is null");
        }

        if (purchaseResult != null && purchaseResult.getStatus() != null && purchaseResult.getStatus().equals("invalid"))
            DuelApp.getInstance().toast(R.string.message_invalid_purchase, Toast.LENGTH_LONG);
    }

//    public void onEvent(OnPurchaseResult alert) {
//
//        if(alert == null) {
//            Log.d("TAG", "alert is null");
//        } else if(alert.getStatus() == null) {
//            Log.d("TAG", "alert.getStatus is null");
//        }
//
//        if (alert != null && alert.getStatus() != null && alert.getStatus().equals("invalid"))
//            DuelApp.getInstance().toast(R.string.message_invalid_purchase, Toast.LENGTH_LONG);
//        else
//            scoresDialog.setUserScore(AuthManager.getCurrentUser().getScore());
//    }

    public static void setAlarmForDuelHour(Context context) {
        Intent intent = new Intent(context, DuelHourStarted.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, DUEL_HOUR_HOUR);
        calendar.set(Calendar.MINUTE, DUEL_HOUR_MINUTE);
        calendar.set(Calendar.SECOND, DUEL_HOUR_SECOND);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                DUEL_HOUR_INTERVAL, pendingIntent);
    }



    protected static Random rand = new Random();
    public static String category;
    public static int NUMBER_OF_QUESTIONS = 6;
    public static List<Question> questionsToAsk = new ArrayList<>();
    protected static int userPoints;
    protected static int opponentPoints;
    static final int NUMBER_OF_AVATARS = 6;

    public boolean canHandleChallengeRequest() {
        return false;
    }

    public boolean isInQuizActivity() {
        return false;
    }

    static protected void shuffleArray(List<String> opts) {
        for (int i = opts.size() - 1; i > 0; i--) {
            int index = rand.nextInt(i) + 1;
            String a = opts.get(index);
            opts.set(index, opts.get(i));
            opts.set(i, a);
        }

        int index = rand.nextInt(opts.size());
        String a = opts.get(0);
        opts.set(0, opts.get(index));
        opts.set(index, a);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TAG", "Parent onResume");
        DuelApp.getInstance().onActivityResumed(this);
        EventBus.getDefault().register(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, DuelApp.getInstance().getIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TAG", "Parent onPause");
        DuelApp.getInstance().onActivityPaused(this);
        EventBus.getDefault().unregister(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    ProgressDialog dialog;
    public void onEvent(OnConnectionStateChanged status) {
        if (status.getState() == OnConnectionStateChanged.ConnectionState.DISCONNECTED ||
                status.getState() == OnConnectionStateChanged.ConnectionState.CONNECTED) {

            if (dialog != null)
                dialog.dismiss();
        }

        if (status.getState() == OnConnectionStateChanged.ConnectionState.DISCONNECTED) {
            Log.d("TAG", "onEvent disconnected");
            ConnectionLostDialog dialog = new ConnectionLostDialog(this);
            dialog.setCancelable(false);
            dialog.show();
        } else if (status.getState() == OnConnectionStateChanged.ConnectionState.CONNECTED) {
            Log.d("TAG", "onEvent connected");
            DuelApp.getInstance().sendMessage(new LoginRequest(CommandType.SEND_USER_LOGIN_REQUEST, DeviceManager.getDeviceId(this)).serialize());
        } else if (status.getState() == OnConnectionStateChanged.ConnectionState.CONNECTING) {
            Log.d("TAG", "onEvent connecting");
            if (showConnectingToServerDialog()) {
                dialog = new ProgressDialog(this);
                dialog.setMessage(getResources().getString(R.string.message_connecting_to_server));
                dialog.setCancelable(false);
                dialog.show();
            }
        }
    }

    public void onEvent(ChallengeRequestDecision challengeRequestDecision) {
        Log.d("TAG", "ParentActivity onEvent");
        Intent i = new Intent(ParentActivity.this, WaitingActivity.class);
        i.putExtra("user_number", challengeRequestDecision.getUserNumber());
        i.putExtra("category", challengeRequestDecision.getCategory());
        i.putExtra("master", false);
        startActivity(i);
    }

    // sub classes can change this configuration by overriding this method. default value is `true`
    // if true then a connecting to server dialog is displayed when connection to server is disconnected.
    //      otherwise, the situation should be handled separately.
    public boolean showConnectingToServerDialog() {
        return true;
    }

    public OnCompleteListener onDecisionMadeListener() {
        return null;
    }


}