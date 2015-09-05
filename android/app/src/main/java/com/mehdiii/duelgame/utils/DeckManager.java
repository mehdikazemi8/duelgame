package com.mehdiii.duelgame.utils;

import com.mehdiii.duelgame.models.Card;
import com.mehdiii.duelgame.models.FlashCard;
import com.mehdiii.duelgame.models.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by Omid on 7/22/2015.
 */
public class DeckManager {
    private static final int MAX_GROUP_COUNT = 5;
    int[] capacities = new int[MAX_GROUP_COUNT + 1];
    Map<Integer, Queue<Card>> groups;
    Card currentCard;
    Wheel wheel;
    DeckSyncer syncer = null;
    FlashCard deck = null;
    String id;

    public void renewCapacities() {
        capacities = new int[]{15, 12, 9, 6, 3};
        if (wheel != null)
            wheel.seekEnd();
    }

    public DeckManager(FlashCard deck, int[] capacities, String id) {
        groups = new HashMap<>();
        this.deck = deck;
        this.id = id;
        List<Card> cards = deck.getCards();


        /// create groups
        for (int i = 0; i < MAX_GROUP_COUNT + 1; i++) {
            groups.put(i, new LinkedList<Card>());
        }

        // add cards to their associated chunks with respect to its capacity
        for (int i = 0; i < cards.size(); i++) {
            groups.get(cards.get(i).getWeight()).add(cards.get(i));
        }

        syncer = new DeckSyncer(this);

        // look for previous browsing history, if not create a bare one.
        if (capacities == null || !hasCapacity())
            renewCapacities();
        else
            this.capacities = capacities;

        // check for wheel start point
        int wheelStart = 0, counter = 0;
        while (this.capacities != null && counter < this.capacities.length && this.capacities[counter++] == 0)
            wheelStart++;
        wheel = new Wheel(0, MAX_GROUP_COUNT - 1, wheelStart);
    }

    public boolean hasCapacity() {
        for (int i = 0; i < capacities.length; i++)
            if (capacities[i] != 0)
                return true;
        return false;
    }

    private boolean endOfTheGame() {
        return deck.getCards().size() == groups.get(5).size();
    }

    public Card hit() {
        if (!hasCapacity())
            renewCapacities();

        if (endOfTheGame()) {
            return new Card(true, 1000, 1000, "تبریک میگم، همه کارتارو یاد تموم کردی.", "حالا برو یه فلش کارت دیگه رو امتحان کن!");
        }

        int counter = 0;

        while ((capacities[wheel.peek()] <= 0 || groups.get(wheel.peek()).size() == 0) && counter < MAX_GROUP_COUNT * 3) {

            if (groups.get(wheel.peek()).size() == 0) {
                capacities[wheel.peek()] = 0;
            }

            wheel.move();

            // counter is used to avoid checking for more than once (possible case of STACKOVERFLOW if groups are totally empty)
            counter++;
        }

        currentCard = groups.get(wheel.peek()).peek();

        capacities[wheel.peek()]--;

        if (onChangeListener != null)
            onChangeListener.onNextCardHit(currentCard);

        return currentCard;
    }

    public void decide(boolean answered) {
        groups.get(wheel.peek()).poll();

        if (answered)
            currentCard.increaseWeight();
        else
            currentCard.resetWeight();

        groups.get(currentCard.getWeight()).add(currentCard);
        syncer.set(new Pair<>(currentCard.getIndex(), currentCard.getWeight()));
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    OnChangeListener onChangeListener = null;

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    public int[] getCapacities() {
        return capacities;
    }

    public String getId() {
        return id;
    }

    public FlashCard getDeck() {
        return deck;
    }

    public void setDeck(FlashCard deck) {
        this.deck = deck;
    }

    public interface OnChangeListener {
        void onNextCardHit(Card card);
    }
}
