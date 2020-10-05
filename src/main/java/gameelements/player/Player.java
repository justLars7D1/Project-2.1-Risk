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
     * @param countryFrom The country the attack originates from.
     * @param countryTo The country the attack is directed to.
     */
    public void onAttackEvent(Country countryFrom, Country countryTo){
        /**
         * Making sure that the origin country belongs to the attacker and that
         * the destination country belongs to the defender.
         */
        if(countriesOwned.contains(countryFrom) && !countriesOwned.contains(countryTo)){
            /**
             *  Making sure that the countries are adjacent.
             */
            if(countryFrom.getNeighboringCountries().contains(countryTo)){
                int attackers = 0;
                int defenders = 0;
                int attack = 0;
                int defend = 0;
                /**
                 * The case that both attacker and defender choose to use 1 Troop.
                 * In this case both players role exactly 1 die.
                 */
                if(attackers == 1 && defenders == 1){
                    List<Integer> attackDice = countryFrom.getOwner().rollDice(1);
                    List<Integer> defendDice = countryTo.getOwner().rollDice(1);

                    // Since only one die is rolled, addressing index 0 is sufficient.
                    attack = attackDice.get(0);
                    defend = defendDice.get(0);

                    //Check for success, failure or draw. Draw counts as failure for attacker.
                    if(attack > defend){
                        countryTo.removeNumSoldiers(1);
                    }
                    else if(attack < defend){
                        countryFrom.removeNumSoldiers(1);
                    }
                    else if(attack == defend){
                        countryFrom.removeNumSoldiers(1);
                    }
                }
                /**
                 * The case that the attacker chooses 1 troop and the defender chooses 2 or more.
                 * In this case, the attacker rolls 1 die, whereas the defender rolls 2 dice,
                 * of which the lower one is discarded.
                 */
                else if (attackers == 1 && defenders > 1){
                    List<Integer> attackDice = countryFrom.getOwner().rollDice(1);
                    List<Integer> defendDice = countryTo.getOwner().rollDice(2);

                    attack= attackDice.get(0);
                    Collections.sort(defendDice); // Sorting the dice in ascending order.
                    defend = defendDice.get(1); // Choosing the second die as it is now the higher one.

                    //Check for success, failure or draw. Draw counts as failure for attacker.
                    if(attack > defend){
                        countryTo.removeNumSoldiers(1);
                    }
                    else if(attack < defend){
                        countryFrom.removeNumSoldiers(1);
                    }
                    else if(attack == defend){
                        countryFrom.removeNumSoldiers(1);
                    }
                }
                /**
                 *  The case that the attacker chooses exactly 2 troops, whereas the defender chooses 1 troop.
                 *  In this case, the attacker rolls 2 dice and the defender rolls 1 die. The lower die of the
                 *  attacker is discarded.
                 */
                else if(attackers == 2 && defenders == 1){
                    List<Integer> attackDice = countryFrom.getOwner().rollDice(2);
                    List<Integer> defendDice = countryTo.getOwner().rollDice(1);

                    defend = defendDice.get(0);
                    Collections.sort(attackDice); // Sorting the dice in ascending order.
                    attack = attackDice.get(1); // Choosing the second die since it is the highest now.

                    //Check for success, failure or draw. Draw counts as failure for attacker.
                    if(attack > defend){
                        countryTo.removeNumSoldiers(1);
                    }
                    else if(attack < defend){
                        countryFrom.removeNumSoldiers(1);
                    }
                    else if(attack == defend){
                        countryFrom.removeNumSoldiers(1);
                    }
                }
                /**
                 *  The case that the attacker chooses more than 2 troops and the defender chooses
                 *  ecatly 1.
                 *  In this case, the attacker rolls 3 dice, whereas the defender rolls 1 die.
                 *  The lower two dice of the attacker are discarded.
                 */
                else if(attackers > 2 && defenders == 1){
                    List<Integer> attackDice = countryFrom.getOwner().rollDice(2);
                    List<Integer> defendDice = countryTo.getOwner().rollDice(1);

                    defend = defendDice.get(0);
                    Collections.sort(attackDice); // Sort the dice in ascending order
                    attack = attackDice.get(2); // Pick the highest die

                    //Check for success, failure or draw. Draw counts as failure for attacker.
                    if(attack > defend){
                        countryTo.removeNumSoldiers(1);
                    }
                    else if(attack < defend){
                        countryFrom.removeNumSoldiers(1);
                    }
                    else if(attack == defend){
                        countryFrom.removeNumSoldiers(1);
                    }
                }
                /**
                 *  The case that the attacker chooses more than 2 troops and the defender chooses more than 1 troop.
                 *  In this case, the attacker rolls 3 dice and the defender rolls 2 dice.
                 *  The lower die of the attacker is discarded and there will be two comparisons of dice.
                 */
                else if(attackers > 2 && defenders > 1){
                    List<Integer> attackDice = countryFrom.getOwner().rollDice(3);
                    List<Integer> defendDice = countryTo.getOwner().rollDice(2);

                    // Sort dice of both in ascending order
                    Collections.sort(attackDice);
                    Collections.sort(defendDice);

                    // Take highest die for both players into comparison
                    attack = attackDice.get(2);
                    defend = defendDice.get(1);

                    //Check for success, failure or draw. Draw counts as failure for attacker.
                    if(attack > defend){
                        countryTo.removeNumSoldiers(1);
                    }
                    else if(attack < defend){
                        countryFrom.removeNumSoldiers(1);
                    }
                    else if(attack == defend){
                        countryFrom.removeNumSoldiers(1);
                    }

                    // Second die comparisson. Take second highest dice of players.
                    attack = attackDice.get(1);
                    defend = defendDice.get(0);

                    //Check for success, failure or draw. Draw counts as failure for attacker.
                    if(attack > defend){
                        countryTo.removeNumSoldiers(1);
                    }
                    else if(attack < defend){
                        countryFrom.removeNumSoldiers(1);
                    }
                    else if(attack == defend){
                        countryFrom.removeNumSoldiers(1);
                    }
                }
            }
        }
        /**
         * Check the number of troops remaining on the defendants country. If it is 0,
         * the attack was successful and the country goes to the atacker.
         */
        if(countryTo.getNumSoldiers() == 0){
            countryTo.setOwner(countryFrom.getOwner());
        }
        //TODO: Test this event
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
