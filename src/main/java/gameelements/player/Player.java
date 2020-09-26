package gameelements.player;

import gameelements.board.Country;
import gameelements.phases.data.AttackEventData;
import gameelements.phases.data.DistributionEventData;
import gameelements.phases.data.FortifyEventData;
import gameelements.phases.data.PlacementEventData;
import settings.Settings;

import java.util.*;

public abstract class Player {

    protected int id;

    protected int numTroopsInInventory;

    protected int[] cards = new int[4];
    protected HashSet<Country> countriesOwned;

    //draft=0, attack=1, fortify=2
    private int PHASE=0;

    protected final int CARDSALLOWED;

    protected Player(int id, int numTroopsInInventory) {
        this.countriesOwned = new HashSet<>();
        this.id = id;
        this.numTroopsInInventory = numTroopsInInventory;
        CARDSALLOWED = Settings.CARDSALLOWED;
    }

    public abstract void onDistributionEvent(Country country);

    public abstract void onPlacementEvent(PlacementEventData data);

    public abstract void onAttackEvent(AttackEventData data);

    public abstract void onFortifyEvent(FortifyEventData data);


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
        for (int card : cards) {
            sum += card;
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

    public int getNumTroopsInInventory() {
        return numTroopsInInventory;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", numTroopsInInventory=" + numTroopsInInventory +
                '}';
    }
}
