package settings;

/**
 * Settings class that provides easy access to all game constants used (for easy modification / access)
 */
public class Settings {

    /**
     * The number of countries that exist within the board
     * Here, the index of the country is it's ID (e.g. Alaska -> ID: 0)
     */
    public static final String[] counties = {"Alaska", // From here North America
            "Alberta (Western Canada)",
            "Central America",
            "Eastern United States",
            "Greenland",
            "Northwest Territory",
            "Ontario (Central Canada)",
            "Quebec (Eastern Canada)",
            "Western United States",
            "Great Britain (Great Britain & Ireland)", // From here Europe
            "Iceland",
            "Northern Europe",
            "Scandinavia",
            "Southern Europe",
            "Ukraine (Eastern Europe, Russia)",
            "Western Europe", // From here Asia
            "Afghanistan",
            "China",
            "India (Hindustan)",
            "Irkutsk",
            "Japan",
            "Kamchatka",
            "Middle East",
            "Mongolia",
            "Siam (Southeast Asia)",
            "Siberia",
            "Ural",
            "Yakutsk",
            "Argentina", // From here South America
            "Brazil",
            "Peru",
            "Venezuela",
            "Congo (Central Africa)", // From here Africa
            "East Africa",
            "Egypt",
            "Madagascar",
            "North Africa",
            "South Africa",
            "Eastern Australia", // From here Australia
            "Indonesia",
            "New Guinea",
            "Western Australia"};

    /**
     * The edges between the countries (Represents a connection from country to country)
     * E.g. Alaska <-> Northwest Territory
     * This means that, if an edge exists between them, troops of a country can invade another country
     */
    public static final String[][] countryEdges = {{"Alaska", "Northwest Territory"} /*etc...*/};

}
