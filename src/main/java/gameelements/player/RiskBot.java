package gameelements.player;

import gameelements.board.Country;
import gameelements.game.Game;
import gameelements.phases.data.AttackEventData;
import environment.FortifyHeuristic;
import settings.BotSettings;
import settings.Settings;

import java.util.*;

public class RiskBot extends Player {

    protected Game currentGame;

    private List<List<Country>> countryDistributionList;

    /**
     * algorithm and strategies for our risk bot
     */
    public RiskBot(int id, int numTroopsInInventory, Game game) {
        super(id, numTroopsInInventory);
        this.countryDistributionList = BotSettings.setupDistributionPriorityList(game.getGameBoard(), game.getNumPlayers());
        this.currentGame = game;
    }

    @Override
    public boolean onDistributionEvent(Country country) {
        country = selectBestCountry();
        return super.onDistributionEvent(country);
    }

    private Country selectBestCountry() {
        for (List<Country> continent: countryDistributionList) {
            for (Country country: continent) {
                if (!country.hasOwner()) {
                    return country;
                }
            }
        }

        // We will place troops on the country with the smallest "security", e.g. highest chance to be overtaken
        Country country = null;
        int troopDifference = Integer.MAX_VALUE;

        for (Country c: this.countriesOwned) {
            // Calculate the number of troops that can attack the country
            int numAttackingTroops = 0;
            List<Country> neighbors = c.getNeighboringCountries();
            for (Country neighbor: neighbors) {
                if (!neighbor.getOwner().equals(this)) {
                    // We do - 1 here, since you can't attack with 1 troop
                    numAttackingTroops += neighbor.getNumSoldiers();
                }
            }

            // Get the total difference in troops, with at most the number of troops that can be placed
            int differenceInTroops = c.getNumSoldiers() - numAttackingTroops;
            differenceInTroops = Math.max(differenceInTroops, -(Settings.TROOPSLIMIT - c.getNumSoldiers()));

            // If the country is the most underarmed of all countries AND the limit is not reached yet,
            // place the troops there
            if (differenceInTroops <= troopDifference && c.getNumSoldiers() != Settings.TROOPSLIMIT) {
                country = c;
                troopDifference = differenceInTroops;
            }
        }

        return country;

    }

    @Override
    public void onPlacementEvent(Country country, int numTroops) {

        //TODO: Add number of opponent countries bordering on tie

        // We will place troops on the country with the smallest "security", e.g. highest chance to be overtaken
        int troopDifference = Integer.MAX_VALUE;
        int numOffensiveCountries = Integer.MIN_VALUE;

        for (Country c: this.countriesOwned) {

            // Calculate the number of troops that can attack the country
            int numAttackingTroops = 0;
            int numAttackingCountries = 0;
            List<Country> neighbors = c.getNeighboringCountries();
            for (Country neighbor: neighbors) {
                if (!neighbor.getOwner().equals(this)) {
                    // We do - 1 here, since you can't attack with 1 troop
                    numAttackingTroops += neighbor.getNumSoldiers() - 1;
                    numAttackingCountries++;
                }
            }

            // Get the total difference in troops, with at most the number of troops that can be placed
            int differenceInTroops = c.getNumSoldiers() - numAttackingTroops;
            differenceInTroops = Math.max(differenceInTroops, -(Settings.TROOPSLIMIT - c.getNumSoldiers()));

            // If the country is the most underarmed of all countries AND the limit is not reached yet,
            // place the troops there
            if (c.getNumSoldiers() != Settings.TROOPSLIMIT) {
                if (differenceInTroops < troopDifference ||
                        (differenceInTroops == troopDifference && numAttackingCountries >= numOffensiveCountries)) {
                    country = c;
                    troopDifference = differenceInTroops;
                    numOffensiveCountries = numAttackingCountries;
                }
            }

        }

        // Place at most the number of troops that are left in the inventory and make sure it places at least one troop
        numTroops = Math.min(numTroopsInInventory, Math.abs(troopDifference));
        if (numTroops == 0) {
            numTroops = numTroopsInInventory;
        }

        if (country == null || numTroopsInInventory == 0) {
            currentGame.nextBattlePhase();
        } else {
            // Execute the action but bound the number of troops
            numTroops = Math.min(numTroops, Settings.TROOPSLIMIT - country.getNumSoldiers());
            super.onPlacementEvent(country, numTroops);
        }
    }

    @Override
    public void onAttackEvent(Country countryFrom, Country countryTo) {
        // Put all the code to pick the right action here
        super.onAttackEvent(countryFrom, countryTo);

        if (countryTo.getOwner().equals(this)) {
            currentGame.addConqueredCountry(countryTo);
        }
        // Add code for deciding end of event phase here (finish attack phase method)
    }

    @Override
    public void onFortifyEvent(Country countryFrom, Country countryTo, int numTroops) {

        //current player in the game to check before editing the troop count that this is the player themselves
        Player currentPLayer = currentGame.getCurrentPlayer();

        //getting all the clusters to pass to the ArmyDistributionFortify method
        List<List<Country>> AllClusters = environment.FortifyHeuristic.getTerritoryClusters(currentGame);

        //retrieving the list of size 42 with the troop assignments >0 for owned countries or 0 otherwise
        List<Integer> desiredTroops = environment.FortifyHeuristic.ArmyDistributionFortify(AllClusters);

        //using this loop to be able to use the i as an index when retrieving other countries to adjust troop counts
        for(int i = 0; i < desiredTroops.size(); i++){
            //fetching the country object
            Country country = currentGame.getGameBoard().getCountryFromID(i);
            //if the owner of the country retrieved is that of the current player then we can adjust the troop countsw
            if(country.getOwner() == currentPLayer){
                //finding the difference between what the country should finally have and what it currently has
                int difference = (int)desiredTroops.get(i) - country.getNumSoldiers();

                //as the checks for whether it is valid is done within the fortify heuristic class we can simply add the difference to the country
                // ex. desired :4 , numSoldiers : 2, difference is 2 so we add 2. desired 5, numSoldiers, 10, difference -5, so we add -5.
                country.addNumSoldiers(difference);
            }
        }

        /**
         * need to loop through the list,
         * for each id get the owner
         *      check if it is the same person
         *      if same then find the difference between the desired and actual
         *      add the difference to the actual (addNumTroops in country class)
         *
         */


        // Put all the code to pick the right action here
        currentGame.nextBattlePhase();
        //super.onFortifyEvent(countryFrom, countryTo, numTroops);
        // Add code for deciding end of event phase here (finish attack phase method)
    }

    @Override
    public void reset(int numTroopsInInventory) {
        super.reset(numTroopsInInventory);
        this.countryDistributionList = BotSettings.setupDistributionPriorityList(currentGame.getGameBoard(), currentGame.getNumPlayers());
    }

    @Override
    public String toString() {
        return "RiskBot{" +
                "id=" + id +
                ", numTroopsInInventory=" + numTroopsInInventory +
                '}';
    }
}
