package board;

import settings.Settings;

import java.util.HashMap;

/**
 * Represents the board of the game, which functions as a simple mapping to countries / continents
 */
public class Board {

    /**
     * Mapping from name of the country to the object (e.g. "Alaska" -> Country(name=Alaska))
     */
    private final HashMap<String, Country> nameToCountryMapping;

    /**
     * Constructor contributes to generating the mapping from name to country
     */
    public Board() {
        this.nameToCountryMapping = new HashMap<>();
        setUpGameBoard();
    }

    /**
     * Retrieve a Country object based on it's ID
     *
     * @param ID The country's ID
     * @return The Country object for that ID
     */
    public Country getCountryFromID(int ID) {
        return getCountryFromName(Settings.counties[ID]);
    }

    /**
     * Retrieve a Country object based on it's name
     *
     * @param name The country's name
     * @return The Country object for that name
     */
    public Country getCountryFromName(String name) {
        return nameToCountryMapping.get(name);
    }

    /**
     * Sets up the board by creating the graph structure of the game
     */
    private void setUpGameBoard() {
        addCountriesToMapping();
        addEdgesToCountries();
    }

    /**
     * Add the countries to the mapping
     */
    private void addCountriesToMapping() {
        for (int i = 0; i < Settings.counties.length; i++) {
            String countryName = Settings.counties[i];
            Country country = new Country(countryName, i);
            nameToCountryMapping.put(countryName, country);
        }
    }

    /**
     * Add the correct edges between the different countries (which represent vertices in a graph)
     */
    private void addEdgesToCountries() {
        for (String[] edge : Settings.countryEdges) {
            Country country1 = nameToCountryMapping.get(edge[0]);
            Country country2 = nameToCountryMapping.get(edge[1]);
            country1.addNeighboringCountry(country2);
            country2.addNeighboringCountry(country1);
        }
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("Board\n");
        for (Country country : nameToCountryMapping.values()) {
            res.append("Country ID ").append(country.getID()).append(": ").append(country).append("\n");
        }
        return res.toString();
    }

}
