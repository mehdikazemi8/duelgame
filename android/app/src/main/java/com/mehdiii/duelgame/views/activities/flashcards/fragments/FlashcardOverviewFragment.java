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
import com.mehdiii.duelgame.utils.DeckManager;

import de.greenrobot.event.EventBus;

/**
 * Created by Omid on 7/22/2015.
 */
public class FlashcardOverviewFragment extends Fragment implements View.OnClickListener {
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

        goButton.setText(DeckManager.hasDeck(getActivity(), card.getId()) ? "GO" : "DOWNLOAD");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_go:
                if (DeckManager.hasDeck(getActivity(), card.getId()))
                    startDeck();
                else
                    startDeckDownload();
                break;
        }
    }

    private void startDeck() {
        // read complete deck including cards, seen, to_ask and etc.
        card = DeckManager.getDeck(getActivity(), card.getId());

        // start flash card
        // TODO
    }

    private void startDeckDownload() {
        DuelApp.getInstance().sendMessage(card.serialize(CommandType.SEND_GET_FLASH_CARD_REQUEST));
    }

    public void onEvent(OnFlashCardReceived c) {
        bindData();
        card = DeckManager.getDeck(getActivity(), card.getId());
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
