package com.mehdiii.duelgame.views.activities.flashcards.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.PurchaseManager;
import com.mehdiii.duelgame.models.FlashCard;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.events.OnFlashCardReceived;
import com.mehdiii.duelgame.models.events.OnPurchaseResult;
import com.mehdiii.duelgame.utils.DeckPersister;
import com.mehdiii.duelgame.utils.FontHelper;

import de.greenrobot.event.EventBus;

/**
 * Created by Omid on 7/22/2015.
 */
public class OverviewFragment extends Fragment implements View.OnClickListener {
    public final static String BUNDLE_PARAM_FLASH_CARD = "bundle_param_flashcard";

    FlashCard card = null;

    TextView ownedTextView;
    TextView titleTextView;
    TextView priceTextView;
    Button goButton;
    Button purchaseButton;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flashcard_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        readArguments();

        // find controls
        ownedTextView = (TextView) view.findViewById(R.id.textView_owned);
        titleTextView = (TextView) view.findViewById(R.id.textView_title);
        priceTextView = (TextView) view.findViewById(R.id.textView_price);
        goButton = (Button) view.findViewById(R.id.button_go);
        purchaseButton = (Button) view.findViewById(R.id.button_purchase);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        // configure controls
        goButton.setOnClickListener(this);
        FontHelper.setKoodakFor(getActivity(), ownedTextView, titleTextView, priceTextView, goButton, purchaseButton);

        bindData();
    }

    private void readArguments() {
        Bundle bundle = getArguments();
        if (bundle == null)
            return;
        card = FlashCard.deserialize(bundle.getString(BUNDLE_PARAM_FLASH_CARD), FlashCard.class);
    }

    private void bindData() {

        if (card.getOwned() == 1)
            purchaseButton.setVisibility(View.INVISIBLE);

        this.ownedTextView.setText(card.getOwned() == 1 ? "OWNED" : "NOT OWNED");
        this.priceTextView.setText(String.valueOf((int) card.getPrice()) + " تومان");
        this.titleTextView.setText(card.getTitle());

        String buttonText = null;
        if (!DeckPersister.hasDeck(getActivity(), card.getId()))
            if (card.getOwned() == 1)
                buttonText = "دریافت";
            else {
                buttonText = "دریافت و امتحان مجانی";
                goButton.setPadding(10, 0, 10, 0);
            }
        else if (card.getOwned() == 1)
            buttonText = "بزن بریم";
        else if (card.getProgress() < card.getPercentFree())
            buttonText = "امتحان کنید";
        else
            goButton.setVisibility(View.INVISIBLE);

        goButton.setText(buttonText);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_go:
                if (DeckPersister.hasDeck(getActivity(), card.getId()))
                    startPracticeIfPossible();
                else
                    startDownloadingDeck();
                break;
        }
    }

    private void startPracticeIfPossible() {
        if (card.getProgress() < card.getPercentFree() || card.getOwned() == 1) {
            // open practice fragment
            Bundle bundle = new Bundle();
            bundle.putString(PracticeFragment.BUNDLE_DECK_ID, card.getId());
            Fragment fragment = Fragment.instantiate(getActivity(), PracticeFragment.class.getName(), bundle);
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom, R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom)
                    .add(R.id.frame_wrapper, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {

            DuelApp.getInstance().toast(R.string.message_heart_is_low, Toast.LENGTH_SHORT);
        }
    }

    private void startDownloadingDeck() {
        this.progressBar.setVisibility(View.VISIBLE);
        turnWaitingMode(true);
        DuelApp.getInstance().sendMessage(card.serialize(CommandType.SEND_GET_FLASH_CARD_REQUEST));
    }

    /**
     * on flash card received
     *
     * @param c receive notice
     */
    public void onEvent(OnFlashCardReceived c) {
        progressBar.setVisibility(View.INVISIBLE);
        turnWaitingMode(false);
        bindData();
        card = DeckPersister.getDeck(getActivity(), card.getId());
        bindData();
    }

    public void onEvent(OnPurchaseResult result) {

    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    private void startPurchase() {
        PurchaseManager.getInstance().startPurchase(9, card.getId());
    }

    private void turnWaitingMode(boolean waiting) {
        goButton.setEnabled(!waiting);
        purchaseButton.setEnabled(!waiting);
    }

}
