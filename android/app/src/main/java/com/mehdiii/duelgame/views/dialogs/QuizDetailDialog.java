package com.mehdiii.duelgame.views.dialogs;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.PurchaseManager;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.quiz.fragments.adapters.QuizSliderAdapter;
import com.mehdiii.duelgame.views.custom.CustomButton;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by frshd on 4/3/16.
 */
public class QuizDetailDialog extends DialogFragment implements View.OnClickListener {

    TextView titleTextView;
    TextView priceText;
    EditText discountText;
    CustomButton subscribeButton;
    ViewPager viewPager;
    ImageButton buttonClose;
    QuizSliderAdapter pagerAdapter;
    CirclePageIndicator indicator;


    boolean subscribedLayout = true;

    int screenW;
    int screenH;

    OnCompleteListener onCompleteListener = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_quiz_detail, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenW = metrics.widthPixels;
        screenH = metrics.heightPixels;

        getDialog().getWindow().setLayout((int) (screenW * 0.9), (int) (screenH * 0.95));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        find(view);
        configure();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void find(View view) {
        titleTextView = (TextView) view.findViewById(R.id.textView_title);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_avatars);
        indicator = (CirclePageIndicator) view.findViewById(R.id.indicator_avatars);
        subscribeButton = (CustomButton) view.findViewById(R.id.button_confirm);
        discountText = (EditText) view.findViewById(R.id.package_discount);
        priceText = (TextView) view.findViewById(R.id.quiz_price);
        buttonClose = (ImageButton) view.findViewById(R.id.button_close);
    }

    private void configure() {
        subscribeButton.setOnClickListener(this);
        buttonClose.setOnClickListener(this);
        priceText.setText(String.format(getString(R.string.subscription_price), AuthManager.getCurrentUser().getSubscriptionPrice()));
        FontHelper.setKoodakFor(getActivity(), titleTextView, discountText);
        pagerAdapter = new QuizSliderAdapter(getChildFragmentManager());
        pagerAdapter.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Object data) {
                getDialog().dismiss();
                if (onCompleteListener != null)
                    onCompleteListener.onComplete(data);
            }
        }, 6 );
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(4);

        indicator.setViewPager(viewPager);

    }

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_close:
                if (onCompleteListener != null)
                    onCompleteListener.onComplete(false);
                break;
            case R.id.button_confirm:
                if(subscribedLayout) {
                    String discountCode = discountText.getText().toString();
                    PurchaseManager.getInstance().startSubscribe(discountCode);
                } else if (onCompleteListener != null)
                    onCompleteListener.onComplete(true);
                break;
        }
        dismiss();
    }

}
