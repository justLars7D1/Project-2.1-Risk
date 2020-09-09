package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import player.Player;
import settings.Settings;

public class Board {

    private final HashMap<String, Country> nameToCountryMapping;

    public Board() {
        this.nameToCountryMapping = new HashMap<>();
        setUpGameBoard();
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

}
