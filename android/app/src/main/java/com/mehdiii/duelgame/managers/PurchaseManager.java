package com.mehdiii.duelgame.managers;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;

import com.android.vending.billing.IInAppBillingService;
import com.google.gson.Gson;
import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.models.BuyNotif;
import com.mehdiii.duelgame.models.PurchaseCafe;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omid on 4/23/2015.
 */
public class PurchaseManager {
    private static PurchaseManager instance;
    private Activity activity;
    IInAppBillingService service;
    BuyNotif buyCommand;
    int requestCode;
    List<PurchaseListener> listeners = new ArrayList<>();

    private void addListener(PurchaseListener listener) {
        listeners.add(listener);
    }

    public void removeListener(PurchaseListener listener) {
        listeners.remove(listener);
    }


    public static void init(Activity activity, IInAppBillingService service, BuyNotif buyCommand, int requestCode) {
        instance = new PurchaseManager();
        instance.activity = activity;
        instance.buyCommand = buyCommand;
        instance.requestCode = requestCode;
        instance.service = service;
        LocalBroadcastManager.getInstance(activity).registerReceiver(instance.receiver, DuelApp.getInstance().getIntentFilter());
    }

    public static PurchaseManager getInstance() {
        return instance;
    }

    BroadcastReceiver receiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            switch (type) {
                case RECEIVE_STARAT_PURCHASE:
                    try {
                        sendPurchaseIntentToBazaar();
                    } catch (RemoteException | JSONException | IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    });

    public synchronized void initiatePurchase() {
        DuelApp.getInstance().sendMessage(
                AuthManager.getCurrentUser().getPurchaseItems().get(0).toPurchaseRequest().serialize(CommandType.SEND_START_PURCHASE));
    }

    private void sendPurchaseIntentToBazaar() throws RemoteException, JSONException, IntentSender.SendIntentException {
        Bundle buyIntentBundle = service.getBuyIntent(
                3, activity.getPackageName(), buyCommand.getSku(), "inapp", "salam-inja-che-bahale");
        PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
        String signature = buyIntentBundle.getString("INAPP_DATA_SIGNATURE");
        String responseCode = buyIntentBundle.getString("RESPONSE_CODE");
        activity.startIntentSenderForResult(pendingIntent.getIntentSender(), requestCode, new Intent(), 0, 0, 0);
    }

    public synchronized void processPurchaseResult(int resultCode, Intent data) {
        int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
        String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
        String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

        if (resultCode == Activity.RESULT_OK) {
            Gson gson = new Gson();

            PurchaseCafe purchase = gson.fromJson(purchaseData, PurchaseCafe.class);
            DuelApp.getInstance().sendMessage(purchase.toPurchaseDone().serialize(CommandType.SEND_PURCHASE_DONE));
            try {
                consumePurchase();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
    }

    public synchronized void consumePurchase() throws RemoteException {
        Bundle ownedItems = service.getPurchases(3, activity.getPackageName(), "inapp", null);

        int response = ownedItems.getInt("RESPONSE_CODE");
        if (response == 0) {
            ArrayList<String> ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
            ArrayList<String> purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
            ArrayList<String> signatureList = ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE");
            String continuationToken = ownedItems.getString("INAPP_CONTINUATION_TOKEN");

            for (int i = 0; i < purchaseDataList.size(); ++i) {
                String purchaseData = purchaseDataList.get(i);
                String signature = signatureList.get(i);
                String sku = ownedSkus.get(i);

                // do something with this purchase information
                // e.g. display the updated list of products owned by user
            }

            if (continuationToken != null)
                consumePurchase();
        }
    }

}
