package gameelements.phases.data;

/**
 * Represents an placement event (player places new troops on land)
 */
public class PlacementEventData implements BattleEventData {

    private int countryID;
    private int numTroops;

    public PlacementEventData(int countryID, int numTroops) {
        this.countryID = countryID;
        this.numTroops = numTroops;
    }

    public int getCountryID() {
        return countryID;
    }

    public int getNumTroops() {
        return numTroops;
    }
}
