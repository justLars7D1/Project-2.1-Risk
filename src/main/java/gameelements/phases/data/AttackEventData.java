package gameelements.phases.data;

/**
 * Represents an attack event (player invades country)
 */
public class AttackEventData implements BattleEventData {

    private int fromCountryID;
    private int toCountryID;

    public AttackEventData(int fromCountry, int toCountry) {
        this.fromCountryID = fromCountry;
        this.toCountryID = toCountry;
    }

    public int getFromCountry() {
        return fromCountryID;
    }

    public int getToCountry() {
        return toCountryID;
    }
}
