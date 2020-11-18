package gameelements.player;

import gameelements.board.Country;
import gameelements.game.Game;
import gameelements.phases.data.AttackEventData;
import settings.BotSettings;

import java.util.ArrayList;
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
        this. countryDistributionList = BotSettings.setupDistributionPriorityList(game.getGameBoard(), game.getNumPlayers());
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
        return (new ArrayList<>(countriesOwned)).get(0);

    }

    @Override
    public void onPlacementEvent(Country country, int numTroops) {
        // Put all the code to pick the right action here
        super.onPlacementEvent(country, numTroops);
        // Add code for deciding end of event phase here (finish attack phase method)
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
        super.onFortifyEvent(countryFrom, countryTo, numTroops);
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
