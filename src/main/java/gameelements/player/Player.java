package gameelements.player;

import gameelements.board.Country;
import settings.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

public abstract class Player {

    protected int id;

    private int numTroopInInventory;

    protected int[] cards = new int[4];
    protected TreeSet<Country> countriesOwned;

    //draft=0, attack=1, fortify=2
    private int PHASE=0;

    private final int CARDSALLOWED;

    protected Player(int id) {
        this.id = id;
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

    public int getId() {
        return id;
    }

    public int getNumTroopInInventory() {
        return numTroopInInventory;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                '}';
    }
}
