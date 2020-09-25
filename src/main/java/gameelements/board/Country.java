package gameelements.board;

import gameelements.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a country in the game
 */
public class Country {

    
    private String name;

    /**
     * ID of a country (integer value from 0 to #countries-1)
     */
    private int ID;

    /**
     * The current owner of the country
     * Is null if there is no owner (setup phase of the game)
     */
    private Player owner;

    /**
     * The amount of soldiers on this country by the current owner
     */
    private int numSoldiers;

    /**
     * The countries that are adjacent to this country
     */
    private List<Country> neighboringCountries;

    public Country(String name, int ID) {
        this.neighboringCountries = new ArrayList<>();
        this.name = name;
        this.ID = ID;
    }

    public void addNeighboringCountry(Country otherCountry) {
        if (!this.neighboringCountries.contains(otherCountry)) this.neighboringCountries.add(otherCountry);
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void setNumSoldiers(int numSoldiers) {
        this.numSoldiers = numSoldiers;
    }

    public void addNumSoldiers(int numSoldiers) {
        this.numSoldiers += numSoldiers;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public Player getOwner() {
        return owner;
    }

    public boolean hasOwner() {
        return owner != null;
    }

    public int getNumSoldiers() {
        return numSoldiers;
    }

    public List<Country> getNeighboringCountries() {
        return neighboringCountries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return ID == country.ID;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", iD=" + ID +
                ", owner=" + owner +
                ", numSoldiers=" + numSoldiers +
                ", numNeighboringCountries=" + neighboringCountries.size() +
                '}';
    }
}
