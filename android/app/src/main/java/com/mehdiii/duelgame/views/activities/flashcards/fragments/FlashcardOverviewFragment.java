package com.mehdiii.duelgame.views.activities.flashcards.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.FlashCard;

/**
 * Created by Omid on 7/22/2015.
 */
public class FlashcardOverviewFragment extends Fragment {
    public final static String BUNDLE_PARAM_FLASH_CARD = "bundle_param_flashcard";

    FlashCard card = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flashcard_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        readArguments();
    }

    private void readArguments() {
        Bundle bundle = getArguments();
        if (bundle == null)
            return;
        card = FlashCard.deserialize(bundle.getString(BUNDLE_PARAM_FLASH_CARD), FlashCard.class);
    }
}
