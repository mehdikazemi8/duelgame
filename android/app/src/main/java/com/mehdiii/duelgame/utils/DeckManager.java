package com.mehdiii.duelgame.utils;

import com.mehdiii.duelgame.models.Card;

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
    Wheel wheel = new Wheel(0, MAX_GROUP_COUNT - 1);

    public void renewCapacities() {
        capacities = new int[]{5, 4, 3, 2, 1};
    }

    public DeckManager(List<Card> cards, int[] capacities) {
        groups = new HashMap<>();

        /// create groups and their capacities
        for (int i = 0; i < MAX_GROUP_COUNT + 1; i++) {
            groups.put(i, new LinkedList<Card>());
        }

        // add cards to their associated chunks
        int level = 0;
        for (int i = 0; i < cards.size(); i++) {
            while (cards.get(i).getWeight() != level)
                level++;

            groups.get(level).add(cards.get(i));
        }

        if (capacities == null)
            renewCapacities();
        else
            this.capacities = capacities;
    }

    public boolean hasCapacity() {
        for (int i = 0; i < capacities.length; i++)
            if (capacities[i] != 0)
                return true;
        return false;
    }

    public Card hit() {
        if (!hasCapacity())
            renewCapacities();

        int counter = 0;
        while ((capacities[wheel.peek()] == 0 || groups.get(wheel.peek()).size() == 0) && counter < MAX_GROUP_COUNT * 3) {
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

    public interface OnChangeListener {
        void onNextCardHit(Card card);
    }
}
