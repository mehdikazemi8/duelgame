package com.mehdiii.duelgame.views.activities.home.fragments.store;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import de.greenrobot.event.EventBus;

/**
 * Created by omid on 4/5/2015.
 */
public class StoreFragment extends FlippableFragment implements View.OnClickListener {

    LinearLayout storeContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        storeContainer = (LinearLayout) view.findViewById(R.id.container_store);

        configure();

        addOffers();
    }

    private void addOffers() {
        for (PurchaseItem item : AuthManager.getCurrentUser().getPurchaseItems()) {
            PurchaseItemView view = new PurchaseItemView(getActivity(), null);
            view.setOnClickListener(this);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.setCaption(item.getTitle());
            view.setType(item.getEntityType());
            view.setPrice(item.getCost().toString());
            view.setTag(item);
            storeContainer.addView(view);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void configure() {

    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
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
            startBuyIntent(item.getId(), item.getCost().getType());
//
//            if (item.getCost().getType() == 1)
//                startBuyIntent(item.getId(), item.getCost().getType());
//            else {
//                // do nothing yet.
//            }
        }
    }

    private void startBuyIntent(int id, int type) {
        EventBus.getDefault().post(new BuyNotification(id, type));
    }

    public void onEvent(PurchaseDone purchase) {
        String message = "";

        switch (purchase.getPurchaseResult()) {
            case COMPLETED:
            case DUPLICATE:
                message = "خرید با موفقیت انجام شد.";
                break;
            case FAILED:
                message = "خرید انجام نشد.";
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