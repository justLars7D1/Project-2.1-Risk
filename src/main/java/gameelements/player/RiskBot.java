package gameelements.player;

import gameelements.board.Country;
import gameelements.phases.data.AttackEventData;

public class RiskBot extends Player {

    /**
     * algorithm and strategies for our risk bot
     */
    public RiskBot(int id, int numTroopsInInventory) {
        super(id, numTroopsInInventory);
    }

    @Override
    public void onDistributionEvent(Country country) {
        // Put all the code to pick the right action here
        super.onDistributionEvent(country);
    }

    @Override
    public void onPlacementEvent(Country country, int numTroops) {
        // Put all the code to pick the right action here
        super.onPlacementEvent(country, numTroops);
        // Add code for deciding end of event phase here (finish attack phase method)
    }

    @Override
    public void onAttackEvent(AttackEventData data) {
        // Put all the code to pick the right action here
        super.onAttackEvent(data);
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
