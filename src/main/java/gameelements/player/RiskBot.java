package gameelements.player;

import gameelements.board.Country;
import gameelements.phases.data.AttackEventData;
import gameelements.phases.data.FortifyEventData;
import gameelements.phases.data.PlacementEventData;

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
    public void onPlacementEvent(PlacementEventData data) {
        // Put all the code to pick the right action here
        super.onPlacementEvent(data);
    }

    @Override
    public void onAttackEvent(AttackEventData data) {
        // Put all the code to pick the right action here
        super.onAttackEvent(data);
    }

    @Override
    public void onFortifyEvent(FortifyEventData data) {
        // Put all the code to pick the right action here
        super.onFortifyEvent(data);
    }

    @Override
    public String toString() {
        return "RiskBot{" +
                "id=" + id +
                ", numTroopsInInventory=" + numTroopsInInventory +
                '}';
    }
}
