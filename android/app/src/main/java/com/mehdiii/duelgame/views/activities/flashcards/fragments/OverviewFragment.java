package com.mehdiii.duelgame.views.activities.flashcards.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.FlashCard;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.events.OnFlashCardReceived;
import com.mehdiii.duelgame.utils.DeckPersister;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flashcard_detail, container, false);
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

        // configure controls
        goButton.setOnClickListener(this);

        bindData();
    }

    private void readArguments() {
        Bundle bundle = getArguments();
        if (bundle == null)
            return;
        card = FlashCard.deserialize(bundle.getString(BUNDLE_PARAM_FLASH_CARD), FlashCard.class);
    }

    private void bindData() {
        this.ownedTextView.setText(card.getOwned() == 1 ? "OWNED" : "NOT OWNED");
        this.priceTextView.setText(String.valueOf(card.getPrice()));
        this.titleTextView.setText(card.getTitle());

        goButton.setText(DeckPersister.hasDeck(getActivity(), card.getId()) ? "GO" : "DOWNLOAD");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_go:
                if (DeckPersister.hasDeck(getActivity(), card.getId()))
                    startPracticing();
                else
                    startDownloadingDeck();
                break;
        }
    }

    private void startPracticing() {
        // open practice fragment
        Bundle bundle = new Bundle();
        bundle.putString(PracticeFragment.BUNDLE_DECK_ID, card.getId());
        Fragment fragment = Fragment.instantiate(getActivity(), PracticeFragment.class.getName(), bundle);
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom)
                .add(R.id.frame_wrapper, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void startDownloadingDeck() {
        DuelApp.getInstance().sendMessage(card.serialize(CommandType.SEND_GET_FLASH_CARD_REQUEST));
    }

    /**
     * on flash card received
     *
     * @param c receive notice
     */
    public void onEvent(OnFlashCardReceived c) {
        bindData();
        card = DeckPersister.getDeck(getActivity(), card.getId());
        bindData();
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


}
