package gameelements.player;

import gameelements.board.Country;
import gameelements.phases.data.AttackEventData;
import gameelements.phases.data.DistributionEventData;
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
    }

    @Override
    public void onPlacementEvent(PlacementEventData data) {
    }

    @Override
    public void onAttackEvent(AttackEventData data) {
    }

    @Override
    public void onFortifyEvent(FortifyEventData data) {
    }

    @Override
    public String toString() {
        return "RiskBot{" +
                "id=" + id +
                ", numTroopsInInventory=" + numTroopsInInventory +
                '}';
    }
}
