package graph;

import settings.Settings;

import java.util.HashMap;

public class Board {

    private final HashMap<String, Country> nameToCountryMapping;

    public Board() {
        this.nameToCountryMapping = new HashMap<>();
        setUpGameBoard();
    }

    public Country getCountryFromID(int ID) {
        return getCountryFromName(Settings.counties[ID]);
    }

    public Country getCountryFromName(String name) {
        return nameToCountryMapping.get(name);
    }

    private void setUpGameBoard() {
        addCountriesToMapping();
        addEdgesToCountries();
    }

    private void addCountriesToMapping() {
        for (int i = 0; i < Settings.counties.length; i++) {
            String countryName = Settings.counties[i];
            Country country = new Country(countryName, i);
            nameToCountryMapping.put(countryName, country);
        }
    }

    private void addEdgesToCountries() {
        for (String[] edge: Settings.countryEdges) {
            Country country1 = nameToCountryMapping.get(edge[0]);
            Country country2 = nameToCountryMapping.get(edge[1]);
            country1.addNeighboringCountry(country2);
            country2.addNeighboringCountry(country1);
        }
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("Board\n");
        for (Country country: nameToCountryMapping.values()) {
            res.append("Country ID ").append(country.getiD()).append(": ").append(country).append("\n");
        }
        return res.toString();
    }

}
