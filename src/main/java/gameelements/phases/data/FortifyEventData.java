package gameelements.phases.data;

/**
 * Represents an fortify event (player moves troops across owned land)
 */
public class FortifyEventData implements BattleEventData {

    private int fromCountryID;
    private int toCountryID;
    private int numTroops;

    public FortifyEventData(int fromCountryID, int toCountryID, int numTroops) {
        this.fromCountryID = fromCountryID;
        this.toCountryID = toCountryID;
        this.numTroops = numTroops;
    }

    public int getFromCountryID() {
        return fromCountryID;
    }

    public int getToCountryID() {
        return toCountryID;
    }

    public int getNumTroops() {
        return numTroops;
    }
}
