package gameelements.player;

import gameelements.board.Country;
import gameelements.game.Game;
import gameelements.phases.data.AttackEventData;
import settings.BotSettings;
import settings.Settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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

        // TODO: Part 2 - All countries have an owner (maybe something with border troops)
        for (Country c: countriesOwned) {
            if (c.getNumSoldiers() < Settings.TROOPSLIMIT) {
                return c;
            }
        }

        // This can never happen, since there are always less troops than the limit on the countries
        return null;

    }

    @Override
    public void onPlacementEvent(Country country, int numTroops) {

        // We will place troops on the country with the smallest "security", e.g. highest chance to be overtaken
        int troopDifference = Integer.MAX_VALUE;

        for (Country c: this.countriesOwned) {

            // Calculate the number of troops that can attack the country
            int numAttackingTroops = 0;
            List<Country> neighbors = c.getNeighboringCountries();
            for (Country neighbor: neighbors) {
                if (!neighbor.getOwner().equals(this)) {
                    // We do - 1 here, since you can't attack with 1 troop
                    numAttackingTroops += neighbor.getNumSoldiers() - 1;
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

        // Place at most the number of troops that are left in the inventory
        numTroops = Math.min(numTroopsInInventory, Math.abs(troopDifference));

        if (country == null) {
            currentGame.nextBattlePhase();
        }

        // Execute the action
        super.onPlacementEvent(country, numTroops);

        // Add code for deciding end of event phase here (finish placement phase method)
        if (numTroopsInInventory == 0) {
            currentGame.nextBattlePhase();
        }

    }

    @Override
    public void onAttackEvent(Country countryFrom, Country countryTo) {
        // Put all the code to pick the right action here
        super.onAttackEvent(countryFrom, countryTo);
        // Add code for deciding end of event phase here (finish attack phase method)
    }

    @Override
    public void onFortifyEvent(Country countryFrom, Country countryTo, int numTroops) {
        // Put all the code to pick the right action here
        currentGame.nextBattlePhase();
        //super.onFortifyEvent(countryFrom, countryTo, numTroops);
        // Add code for deciding end of event phase here (finish attack phase method)
    }

    @Override
    public String toString() {
        return "RiskBot{" +
                "id=" + id +
                ", numTroopsInInventory=" + numTroopsInInventory +
                '}';
    }
}
