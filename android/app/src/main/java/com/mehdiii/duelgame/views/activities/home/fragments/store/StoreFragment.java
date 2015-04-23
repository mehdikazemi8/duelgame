package com.mehdiii.duelgame.views.activities.home.fragments.store;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.BuyNotif;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.activities.home.fragments.FlipableFragment;

import de.greenrobot.event.EventBus;

/**
 * Created by omid on 4/5/2015.
 */
public class StoreFragment extends FlipableFragment implements View.OnClickListener {

    Button heartButton;
    Button dimondButton;
    Button expButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        heartButton = (Button) view.findViewById(R.id.button_buy_hearts);
        dimondButton = (Button) view.findViewById(R.id.button_buy_diamons);
        expButton = (Button) view.findViewById(R.id.button_buy_exp);

        configure();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    private void configure() {
        FontHelper.setKoodakFor(getActivity(), heartButton, dimondButton, expButton);

        heartButton.setOnClickListener(this);
        dimondButton.setOnClickListener(this);
        expButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_buy_diamons:
            case R.id.button_buy_exp:
            case R.id.button_buy_hearts:
                startBuyIntent();
                break;
        }
    }

    private void startBuyIntent() {
        EventBus.getDefault().post(new BuyNotif("test-heart"));
    }
}