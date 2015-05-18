package com.mehdiii.duelgame.views.activities.home.fragments.store;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.BuyNotification;
import com.mehdiii.duelgame.models.events.OnDiamondChangeNotice;
import com.mehdiii.duelgame.models.PurchaseDone;
import com.mehdiii.duelgame.models.PurchaseItem;
import com.mehdiii.duelgame.views.activities.home.fragments.FlippableFragment;
import com.mehdiii.duelgame.views.custom.PurchaseItemView;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by omid on 4/5/2015.
 */
public class StoreFragment extends FlippableFragment implements View.OnClickListener {

    LinearLayout storeContainer;
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        storeContainer = (LinearLayout) view.findViewById(R.id.container_store);

        addOffers();
    }

    private void addOffers() {
        int previousType = -1;
        boolean newLine = true;
        LinearLayout linearLayout = null;
        int parity = 0;
        List<PurchaseItem> purchaseItems = AuthManager.getCurrentUser().getPurchaseItems();
        int counter = 1;
//        PurchaseItem item : AuthManager.getCurrentUser().getPurchaseItems()
        while (counter < purchaseItems.size()) {
            PurchaseItem item = purchaseItems.get(counter);
            if (counter % 2 == 0)
                counter += 3;
            else
                counter -= 1;

            if (parity == 2) {
                parity = 0;
                newLine = true;
            }

            if (newLine) {
                if (linearLayout != null)
                    storeContainer.addView(linearLayout);
            }

            if (previousType != -1 && item.getEntityType() != previousType) {
                addDelimiter();
                newLine = true;
            }

            if (newLine) {
                linearLayout = new PurchaseItemView(getActivity());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER);
                newLine = false;
                parity = 0;
            }

            PurchaseItemView view = new PurchaseItemView(getActivity(), null);
            view.setGravity(Gravity.CENTER);
            view.setOnClickListener(this);
            view.setCaption(item.getTitle());
            view.setType(item.getEntityType());
            view.setPrice(item.getCost().toString());
            view.setTag(item);
            linearLayout.addView(view, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            previousType = item.getEntityType();
            parity++;
        }
        if (linearLayout != null)
            storeContainer.addView(linearLayout);
    }

    private void addDelimiter() {
        View view = new View(getActivity());
        view.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        view.setPadding(0, 30, 0, 30);
        view.setBackgroundColor(getResources().getColor(R.color.blue_dark));
        storeContainer.addView(view);
    }


    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);

        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        if (view instanceof PurchaseItemView) {
            PurchaseItem item = ((PurchaseItemView) view).getTag();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("لطفا کمی صبر کنید");
            dialog.setCancelable(false);
            dialog.show();
            EventBus.getDefault().post(new BuyNotification(item.getId(), item.getCost().getType()));
        }
    }

    public void onEvent(PurchaseDone purchase) {
        if (dialog != null)
            dialog.dismiss();

        String message = "";

        switch (purchase.getPurchaseResult()) {
            case COMPLETED:
            case DUPLICATE:
                message = "خرید با موفقیت انجام شد.";
                break;
            case FAILED:
                message = "خرید انجام نشد.";
                break;
            case NOT_ENOUGH:
                message = "الماس به اندازه کافی نداری!";
                break;
            case UNKNOWN:
                message = "مشکل نامشخص!";
                break;
        }

        // show result dialog
        new PurchaseResultDialog(getActivity(), message).show();

        // notify that diamond is changed to a new value
        if (purchase.getPurchaseResult() == PurchaseDone.PurchaseResult.COMPLETED) {
            AuthManager.getCurrentUser().setDiamond(purchase.getDiamond());
            EventBus.getDefault().post(new OnDiamondChangeNotice(purchase.getDiamond()));
        }
    }
}