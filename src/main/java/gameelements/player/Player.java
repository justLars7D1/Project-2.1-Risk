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
     * @return Whether the action was successful
     */
    public boolean onDistributionEvent(Country country) {
        boolean success = false;
        if (!country.hasOwner() || countriesOwned.contains(country) && numTroopsInInventory - 1 >= 0) {
            country.setOwner(this);
            country.addNumSoldiers(Settings.NUMTROOPSONDISTRIBUTION);
            numTroopsInInventory -= Settings.NUMTROOPSONDISTRIBUTION;
            countriesOwned.add(country);
            success = true;
        }
        return success;
    }

    /**
     * Represents the action the player takes on a placement event
     * @param country The country to put troops on
     * @param numTroops The number of troops to put on the country
     */
    public void onPlacementEvent(Country country, int numTroops) {
        if (countriesOwned.contains(country) && numTroopsInInventory - numTroops >= 0) {
            country.addNumSoldiers(numTroops);
            numTroopsInInventory -= numTroops;
        }
    }

    /**
     * Represents the action the player takes on an attack event
     * @param data The data needed to take an action
     */
    public void onAttackEvent(AttackEventData data) {
        //TODO: Implement this event
    }

    /**
     * Represents the action the player takes on a fortify event
     * @param countryFrom The country to move troops from
     * @param countryTo The country to move troops to
     * @param numTroops The number of troops to move
     */
    public void onFortifyEvent(Country countryFrom, Country countryTo, int numTroops) {
        if (countriesOwned.contains(countryFrom) && countriesOwned.contains(countryTo) &&
                                                    countryFrom.getNumSoldiers() - numTroops >= 1) {
            boolean existsPathFromCountryToCountry = existsCountryPath(countryFrom, countryTo);
            System.out.println(existsPathFromCountryToCountry);
            if (existsPathFromCountryToCountry) {
                countryFrom.removeNumSoldiers(numTroops);
                countryTo.addNumSoldiers(numTroops);
                //TODO: See if I'm not forgetting anything
            }
        }
    }

    /**
     * Check if there exists a path from an owned country to another owned country s.t. all countries are owned
     * by the player.
     * (We have an undirected acyclic graph)
     * @param countryFrom The first vertex in the path
     * @param countryTo The last vertex in the path
     * @return Whether the path exists
     */
    private boolean existsCountryPath(Country countryFrom, Country countryTo) {
        boolean pathExists = false;
        Queue<Country> pathQueue = new ArrayDeque<>();
        pathQueue.add(countryFrom);
        while (!pathQueue.isEmpty() && !pathExists) {
            Country c = pathQueue.poll();
            for(Country neighbor: c.getNeighboringCountries()) {
                if (neighbor.getOwner().equals(this)) {
                    pathQueue.add(neighbor);
                }
            }
            if (c.equals(countryTo)) {
                pathExists = true;
            }
        }

        return pathExists;
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

    private void removeCountry(Country country){
        countriesOwned.remove(country);
    }

    public int getNumCountriesOwned() {
        return countriesOwned.size();
    }

    public HashSet<Country> getCountriesOwned(){ return countriesOwned; }

    public int getId() {
        return id;
    }

    public void setNumTroopsInInventory(int numTroopsInInventory) {
        this.numTroopsInInventory = numTroopsInInventory;
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
