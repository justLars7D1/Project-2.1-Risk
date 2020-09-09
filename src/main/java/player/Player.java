package player;

import board.Country;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

public abstract class Player {

    private final int CARDSALLOWED;
    private int[] cards = new int[4];
    private TreeSet<Country> countriesOwned;

    protected Player(int cardsallowed) {
        CARDSALLOWED = cardsallowed;
    }

    private List<Integer> rollDice(int dice) {
        Random die = new Random();
        List<Integer> diceResults = new ArrayList<>();
        for (int i = 0; i < dice; i++) {
            int d = die.nextInt(5);
            diceResults.add(d);
        }
        return diceResults;
    }

    private boolean fullHand() {
        int sum = 0;
        for (int i = 0; i < cards.length; i++) {
            sum += cards[i];
        }
        return sum >= CARDSALLOWED;
    }

    private void endTurn() {
    }

    private void nextPhase() {
    }

}
