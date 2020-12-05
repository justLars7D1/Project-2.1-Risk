package gameelements.player;

import UI.InGame.BoardMap;
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
        boolean hasLessTroopsThanLimit = country.getNumSoldiers() < Settings.TROOPSLIMIT;
        if ((!country.hasOwner() || countriesOwned.contains(country) && numTroopsInInventory - 1 >= 0) && hasLessTroopsThanLimit) {
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
        boolean hasLessTroopsThanLimit = country.getNumSoldiers() + numTroops <= Settings.TROOPSLIMIT;
        if (countriesOwned.contains(country) && numTroopsInInventory - numTroops >= 0 && hasLessTroopsThanLimit) {
            country.addNumSoldiers(numTroops);
            numTroopsInInventory -= numTroops;
        }
    }

    /**
     * Represents the action the player takes on an attack event
     * @param countryFrom The country the attack originates from.
     * @param countryTo The country the attack is directed to.
     */
    public void onAttackEvent(Country countryFrom, Country countryTo){
        // Ensure countries are owned and not owned, and if they're adjacent
        if(countriesOwned.contains(countryFrom) && !countriesOwned.contains(countryTo) && countryFrom.getNeighboringCountries().contains(countryTo)){

            // Calculate the number of attackers
            int attackers = countryFrom.getNumSoldiers() - 1;
            int defenders = countryTo.getNumSoldiers();

            // Check if there's enough troops to attack/defend with (should be true by default for defenders)
            if (attackers > 0 && defenders > 0) {

                // Find the number of dices to be rolled
                int numDiceAttacker = Math.min(attackers, Settings.MAXNUMDICESROLLEDATTACKER);
                int numDiceDefender = Math.min(defenders, Settings.MAXNUMDICESROLLEDDEFENDER);
                int minNumDiceRolled = Math.min(numDiceAttacker, numDiceDefender);

                // Create list of uniformly distributed dice rolls for attacker and defender
                // Here the number of attacker dice range from 1 to 3 and for the defenders from 1 to 2
                List<Integer> attackerDiceRolls = rollDice(numDiceAttacker);
                List<Integer> defenderDiceRolls = rollDice(numDiceDefender);
                attackerDiceRolls.sort(Collections.reverseOrder());
                defenderDiceRolls.sort(Collections.reverseOrder());

                for (int i = 0; i < minNumDiceRolled; i++) {
                    int attackerRoll = attackerDiceRolls.get(i);
                    int defenderRoll = defenderDiceRolls.get(i);
                    if (attackerRoll > defenderRoll) {
                        countryTo.removeNumSoldiers(1);
                    } else {
                        countryFrom.removeNumSoldiers(1);
                    }
                }

                boolean conquered = checkCountryConquer(countryFrom, countryTo);
                if (Settings.ATTACKUNTILWINORLOSE && !conquered) onAttackEvent(countryFrom, countryTo);

            }

        }
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
        HashSet<Country> seenCountries = new HashSet<>();
        Queue<Country> pathQueue = new ArrayDeque<>();
        pathQueue.add(countryFrom);
        seenCountries.add(countryFrom);
        while (!pathQueue.isEmpty() && !pathExists) {
            Country c = pathQueue.poll();
            for(Country neighbor: c.getNeighboringCountries()) {
                if (neighbor.getOwner().equals(this) && !seenCountries.contains(neighbor)) {
                    pathQueue.add(neighbor);
                    seenCountries.add(neighbor);
                }
            }
            if (c.equals(countryTo)) {
                pathExists = true;
            }
        }

        return pathExists;
    }

    /**
     * Checks if a country has been conquered by the player
     * @param countryFrom The country the attack came from
     * @param countryTo The country that was attacked
     */
    public boolean checkCountryConquer(Country countryFrom, Country countryTo) {
        if(countryTo.getNumSoldiers() == 0){
            // Remove from countries owned of old owner and add it for the new owner
            countryTo.getOwner().countriesOwned.remove(countryTo);
            countriesOwned.add(countryTo);

            // Set the owner in the country object
            countryTo.setOwner(this);

            // Note - This is okay, since the attacker must've won in this case, so there's at least one troop left
            // Transfer one troop from country
            countryFrom.removeNumSoldiers(1);
            countryTo.addNumSoldiers(1);

            return true;
        }

        return false;
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
