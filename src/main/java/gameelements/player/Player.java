package gameelements.player;

import gameelements.board.Country;
import gameelements.phases.data.AttackEventData;
import gameelements.phases.data.DistributionEventData;
import gameelements.phases.data.FortifyEventData;
import gameelements.phases.data.PlacementEventData;
import settings.Settings;

import java.util.*;

/**
 * Represents a player in the game
 */
public abstract class Player {

    /**
     * The ID of the player
     */
    protected int id;

    /**
     * The current number of troops in the player's inventory
     */
    protected int numTroopsInInventory;

    /**
     * The number of cards the player owns
     */
    protected int numCards;

    /**
     * The countries that a player owns
     */
    protected HashSet<Country> countriesOwned;

    protected Player(int id, int numTroopsInInventory) {
        this.countriesOwned = new HashSet<>();
        this.id = id;
        this.numTroopsInInventory = numTroopsInInventory;
    }

    /**
     * Represents the action the player takes on a distribution event
     * @param country The country to distribute troops on
     */
    public void onDistributionEvent(Country country) {
        if (!country.hasOwner() || countriesOwned.contains(country)) {
            country.setOwner(this);
            country.addNumSoldiers(Settings.NUMTROOPSONDISTRIBUTION);
            numTroopsInInventory -= Settings.NUMTROOPSONDISTRIBUTION;
            countriesOwned.add(country);
        }
    }

    /**
     * Represents the action the player takes on a placement event
     * @param data The data needed to take an action
     */
    public void onPlacementEvent(PlacementEventData data) {
    }

    /**
     * Represents the action the player takes on an attack event
     * @param data The data needed to take an action
     */
    public void onAttackEvent(AttackEventData data) {
    }

    /**
     * Represents the action the player takes on a fortify event
     * @param data The data needed to take an action
     */
    public void onFortifyEvent(FortifyEventData data) {
    }

    /**
     * Represents the even of rolling dice
     * @param dice The number of dice to roll
     * @return The results of the roll
     */
    private List<Integer> rollDice(int dice) {
        Random die = new Random();
        List<Integer> diceResults = new ArrayList<>();
        for (int i = 0; i < dice; i++) {
            int d = die.nextInt(5);
            diceResults.add(d);
        }
        return diceResults;
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
