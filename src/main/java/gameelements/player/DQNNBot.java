package gameelements.player;

import gameelements.board.Country;
import gameelements.game.Game;

public class DQNNBot extends RiskBot {

    /**
     * algorithm and strategies for our risk bot
     */
    public DQNNBot(int id, int numTroopsInInventory, Game game) {
        super(id, numTroopsInInventory, game);
    }

    @Override
    public boolean onDistributionEvent(Country country) {
        // Put all the code to pick the right action here
        return super.onDistributionEvent(country);
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

}
