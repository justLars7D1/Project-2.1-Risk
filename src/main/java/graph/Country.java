package graph;

import player.Player;

import java.util.ArrayList;
import java.util.List;

public class Country {

    private String name;
    private int iD;

    private Player owner;
    private int numSoldiers;

    private List<Country> neighboringCountries;

    public Country(String name, int iD) {
        this.neighboringCountries = new ArrayList<>();
        this.name = name;
        this.iD = iD;
    }

    public void addNeighboringCountry(Country otherCountry) {
        this.neighboringCountries.add(otherCountry);
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void setNumSoldiers(int numSoldiers) {
        this.numSoldiers = numSoldiers;
    }

    public String getName() {
        return name;
    }

    public int getiD() {
        return iD;
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
        return iD == country.iD;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", iD=" + iD +
                ", owner=" + owner +
                ", numSoldiers=" + numSoldiers +
                ", numNeighboringCountries=" + neighboringCountries.size() +
                '}';
    }
}
