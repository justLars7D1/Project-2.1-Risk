package player;

import board.Country;
import settings.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

public abstract class Player {

    private int[] cards = new int[4];
    private TreeSet<Country> countriesOwned;

    //draft=0, attack=1, fortify=2
    private int PHASE=0;

    private final int CARDSALLOWED;

    protected Player() {
        CARDSALLOWED = Settings.CARDSALLOWED;
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

    private int[] seeCards(){
        return cards;
    }

    private void endTurn() {
    }

    private void nextPhase() {
    }
    private void addCountry(Country country){
        countriesOwned.add(country);
    }

}
