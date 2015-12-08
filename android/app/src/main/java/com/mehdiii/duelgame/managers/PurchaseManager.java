package com.mehdiii.duelgame.managers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.models.BuyNotification;
import com.mehdiii.duelgame.models.PurchaseCreated;
import com.mehdiii.duelgame.models.PurchaseDone;
import com.mehdiii.duelgame.models.PurchaseItem;
import com.mehdiii.duelgame.models.PurchaseRequest;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.events.OnPurchaseResult;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.IabHelper;
import com.mehdiii.duelgame.utils.IabResult;
import com.mehdiii.duelgame.utils.Inventory;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.utils.Purchase;

import org.json.JSONException;

import de.greenrobot.event.EventBus;

/**
 * Created by omid on 4/23/2015.
 */
public class PurchaseManager {
    public static final String BASE_64_PUBLIC_KEY = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDQ6R5cmQIA0CRQVsEQoMO5sbONC3Jxuf0ng05fRHvbGakNhDorp86k5KY7ikaHV8BbndgLdjROp/DX/Y8wJaJhdlmoyPfBoTqTIQofhEuZKVAKq6Z5qiIL/fTvx357nME+YTPda4SvrXQ8/lAoasf2bRVdpq2spsmP1HNa8xAs/WnJzF7ShGr84cvIMmo4cOVSi/P3EX/CzXpyU8nwbVW0Mkw6lJ+N+5vV2kun2PUCAwEAAQ==";
    public static final String TAG = "PURCHASE_MANAGER";
    private static PurchaseManager instance;
    static final int RC_REQUEST = 10001;
    private Activity activity;
    int requestCode;
    Purchase purchasedItem = null;
    IabHelper helper;
    boolean isBusy = false;
    static PurchaseItem currentPurchase;
    String cardId;

    public static void changeActivity(Activity activity) {
        instance.activity = activity;
        Log.d("PURCHASE_MANAGER", "initiation finished");
    }

    public static void init(Context context) {
        Log.d("PURCHASE_MANAGER", "purchase module is starting.");
        instance = new PurchaseManager();
//        instance.activity = activity;
        instance.helper = new IabHelper(context, BASE_64_PUBLIC_KEY);
        instance.helper.enableDebugLogging(true, "PURCHASE_MANAGER");
        instance.helper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
//                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (instance.helper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
//                Log.d(TAG, "Setup successful. Querying inventory.");
//                instance.helper.queryInventoryAsync(instance.mGotInventoryListener);
            }
        });
        LocalBroadcastManager.getInstance(context).registerReceiver(instance.receiver, DuelApp.getInstance().getIntentFilter());
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (helper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
//                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // TODO remove next line
            if (AuthManager.getCurrentUser().getPurchaseItems() != null)
                for (PurchaseItem item : AuthManager.getCurrentUser().getPurchaseItems()) {
                    // Check for gas delivery -- /if we own gas, we should fill up the tank immediately
                    Purchase gasPurchase = inventory.getPurchase(item.getSku());
                    if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                        Log.d(TAG, "We have gas. Consuming it.");
//                    helper.consumeAsync(inventory.getPurchase(item.getSku()), mConsumeFinishedListener);
                        return;
                    }
                }
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);
        }
    };

    /**
     * Verifies the developer payload of a purchase.
     */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

    String purchaseId;

    BroadcastReceiver receiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            switch (type) {
                case RECEIVE_START_PURCHASE:
                    try {
                        PurchaseCreated purchase = BaseModel.deserialize(json, PurchaseCreated.class);

                        if (purchase != null)
                            purchaseId = purchase.getPurchaseId();
                        sendToBazaarForPayment(purchase);
                    } catch (RemoteException | JSONException | IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                    break;
                case RECEIVE_PURCHASE_DONE:
                    PurchaseDone purchaseDone = BaseModel.deserialize(json, PurchaseDone.class);
                    if (purchaseDone != null) {

                        if (purchaseDone.getStatus().equals("invalid")) {
                            EventBus.getDefault().post(new OnPurchaseResult(purchaseDone.getStatus()));
                            EventBus.getDefault().post(purchaseDone);

                            return;
                        } else {
                            AuthManager.getCurrentUser().changeConfiguration(DuelApp.getInstance(), purchaseDone.getDiamond(), purchaseDone.getHeart(), purchaseDone.isExtremeHeart(), purchaseDone.getScoreFactor());
                            purchaseDone.setPurchaseItem(currentPurchase);

                            try {
                                consumePurchase();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        EventBus.getDefault().post(new OnPurchaseResult());
                        EventBus.getDefault().post(purchaseDone);
                    }

                    break;
            }
        }
    });


    public synchronized void startPurchase(int id) {
        startPurchase(id, "");
    }

    public synchronized void startPurchase(int id, String cardId) {
//        if (!isBusy) {
        isBusy = true;

        this.cardId = cardId;
        currentPurchase = AuthManager.getCurrentUser().findPurchaseById(id);
        if (currentPurchase != null) {
            PurchaseRequest bundle;

            if (cardId != null && !cardId.isEmpty())
                bundle = currentPurchase.toPurchaseRequest(cardId);
            else
                bundle = currentPurchase.toPurchaseRequest();

            DuelApp.getInstance().sendMessage(bundle.serialize(CommandType.SEND_START_PURCHASE));
        }
//        }
    }


    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.getResponse() == IabHelper.BILLING_RESPONSE_RESULT_OK) {
                Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
                isBusy = false;
                purchasedItem = purchase;

                // if we were disposed of in the meantime, quit.
                if (helper == null || result.isFailure() || !verifyDeveloperPayload(purchase))
                    return;

                PurchaseCreated bundle;
                if (cardId != null && !cardId.isEmpty())
                    bundle = new PurchaseCreated(purchaseId, purchase.getOrderId(), cardId);
                else
                    bundle = new PurchaseCreated(purchaseId, purchase.getOrderId());

                DuelApp.getInstance().sendMessage(bundle.serialize(CommandType.SEND_PURCHASE_DONE));

                Log.d(TAG, "Purchase successful.");
            }
        }
    };

    public static PurchaseManager getInstance() {
        return instance;
    }

    public void useDiamond(BuyNotification purchaseNotif) {
        currentPurchase = AuthManager.getCurrentUser().findPurchaseById(purchaseNotif.getId());
        if (currentPurchase != null) {
            DuelApp.getInstance().sendMessage(currentPurchase.toPurchaseRequest().serialize(CommandType.SEND_START_PURCHASE));
        }
    }


    private void sendToBazaarForPayment(PurchaseCreated purchaseDone) throws RemoteException, JSONException, IntentSender.SendIntentException {
        if (currentPurchase != null) {
            if (helper != null)
                helper.flagEndAsync();

            try {
                helper.launchPurchaseFlow(activity, currentPurchase.getSku(), RC_REQUEST,
                        mPurchaseFinishedListener, purchaseDone.getPurchaseId());
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }

        }
    }

    public synchronized void consumePurchase() throws RemoteException {
        if (purchasedItem == null)
            return;

        helper.consumeAsync(purchasedItem, mConsumeFinishedListener);
    }

    public boolean handleActivityResult(int resultCode, Intent data) {
        // Pass on the activity result to the helper for handling
        if (!helper.handleActivityResult(RC_REQUEST, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            return false;
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }

        return true;
    }


}
