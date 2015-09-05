package com.mehdiii.duelgame.views.activities.flashcards.fragments;

import android.animation.Animator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.Card;
import com.mehdiii.duelgame.models.FlashCard;
import com.mehdiii.duelgame.utils.DeckManager;
import com.mehdiii.duelgame.utils.DeckPersister;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.custom.CardView;

/**
 * Created by Omid on 7/22/2015.
 */
public class PracticeFragment extends Fragment implements View.OnClickListener {

    private final static int ANIMATION_DURATION = 500;
    GestureDetectorCompat gestureDetector;
    CardView firstCard, secondCard, mainCard;
    Button positiveButton;
    Button negativeButton;
    DeckManager deckManager;
    FlashCard deck;
    public static final String BUNDLE_DECK_ID = "bundle_deck_id";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flashcard_practice, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firstCard = (CardView) view.findViewById(R.id.cardView_main);
        secondCard = (CardView) view.findViewById(R.id.cardVIew_secondary);

        gestureDetector = new GestureDetectorCompat(getActivity(), new GestureDetector());
        positiveButton = (Button) view.findViewById(R.id.button_positive);
        negativeButton = (Button) view.findViewById(R.id.button_negative);

        negativeButton.setOnClickListener(this);
        positiveButton.setOnClickListener(this);

        FontHelper.setKoodakFor(getActivity(), negativeButton, positiveButton);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mainCard = firstCard;
        secondCard.setTranslationX(metrics.widthPixels);

        readArguments();

//        deck.organize();
        deckManager = new DeckManager(deck, deck.getToAsk(), deck.getId());
        hit(firstCard, deckManager.hit());
    }

    private void hit(CardView view, Card card) {
        view.bind(card);

        if (card.isDisabled()) {
            this.positiveButton.setVisibility(View.INVISIBLE);
            this.negativeButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DeckPersister.saveDeck(getActivity(), deck);
    }

    private void readArguments() {
        Bundle bundle = getArguments();
        if (bundle == null)
            return;
        String id = bundle.getString(BUNDLE_DECK_ID);
        deck = DeckPersister.getDeck(getActivity(), id);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_positive:
                moveNext(true);
                break;
            case R.id.button_negative:
                moveNext(false);
                break;
        }
    }

    int parity = 0;

    private void moveNext(boolean answered) {
        final DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();

        int apiVersion = android.os.Build.VERSION.SDK_INT;

        if (16 <= apiVersion) {
            firstCard.animate().translationXBy(-1 * metrics.widthPixels)
                    .setDuration(1000)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setListener(new CardAnimationListener(firstCard))
                    .withLayer();

            secondCard.animate().translationXBy(-1 * metrics.widthPixels)
                    .setDuration(1000)
                    .setListener(new CardAnimationListener(secondCard))
                    .withLayer();
        } else {

            firstCard.animate().translationXBy(-1 * metrics.widthPixels)
                    .setDuration(1000)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setListener(new CardAnimationListener(firstCard));

            secondCard.animate().translationXBy(-1 * metrics.widthPixels)
                    .setDuration(1000)
                    .setListener(new CardAnimationListener(secondCard));
        }

        if (parity == 0) {
            parity = 1;
            mainCard = secondCard;
        } else {
            mainCard = firstCard;
            parity = 0;
        }

        deckManager.decide(answered);
        mainCard.reset();
        hit(mainCard, deckManager.hit());
    }

    class CardAnimationListener implements Animator.AnimatorListener {
        CardView cardView;

        public CardAnimationListener(CardView cardView) {
            this.cardView = cardView;
        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {
            new ParametricRunnable(cardView).run();
        }

        @Override
        public void onAnimationStart(Animator animator) {
            negativeButton.setEnabled(false);
            positiveButton.setEnabled(false);
//            negativeButton.animate().alpha(0).setDuration(ANIMATION_DURATION);
//            positiveButton.animate().alpha(0).setDuration(ANIMATION_DURATION);
        }
    }

    private class ParametricRunnable implements Runnable {
        CardView card;

        public ParametricRunnable(CardView card) {
            this.card = card;
        }

        @Override
        public void run() {
            final DisplayMetrics metrics = getResources().getDisplayMetrics();
            if (card.getX() == metrics.widthPixels * -1) {
                card.setTranslationX(metrics.widthPixels);
            }

//            negativeButton.animate().alpha(100).setDuration(ANIMATION_DURATION);
//            positiveButton.animate().alpha(100).setDuration(ANIMATION_DURATION);
            negativeButton.setEnabled(true);
            positiveButton.setEnabled(true);
        }
    }

    private class GestureDetector extends android.view.GestureDetector.SimpleOnGestureListener {
        private static final float SWIPE_THRESHOLD = 20f;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();

            if (Math.abs(diffX) > Math.abs(diffY) && SWIPE_THRESHOLD < Math.abs(diffX)) {
                if (diffX > 0)
                    mainCard.swipeRight();
                else
                    mainCard.swipeLeft();
                return true;
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}
