package gameelements.phases.data;

/**
 * Represents the data that is passed for a distribution event
 */
public class DistributionEventData implements GameEventData {

    private int countryID;

    public DistributionEventData(int countryID) {
        this.countryID = countryID;
    }

    public int getCountryID() {
        return countryID;
    }
}
