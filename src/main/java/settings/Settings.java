package settings;

import java.util.HashMap;

/**
 * Settings class that provides easy access to all game constants used (for easy modification / access)
 */
public class Settings {
    /**
     *  Defines the maximum number of cards that can be held on hand by a gameelements.player.
     */
    public static final int CARDSALLOWED = 5;

    /**
     *  The minimum number of troops a player gets each battle phase turn
     */
    public static final int MINNUMTROOPSPERTURN = 2;

    /**
     *  Factor at which troops are calculated based on the country each turn of the battle phase
     */
    public static final int TROOPSPERCOUNTRYDIVIDER = 3;

    /**
     * The number of troops that are placed at once at the first phase of the game (distribution phase)
     */
    public static final int NUMTROOPSONDISTRIBUTION = 1;

    /**
     * The number of countries that exist within the gameelements.board
     * Here, the index of the country is it's ID (e.g. Alaska -> ID: 0)
     */
    public static final String[] countries = {
            // From here Africa
            "Congo (Central Africa)",
            "East Africa",
            "Egypt",
            "Madagascar",
            "North Africa",
            "South Africa",  // Index = 5

            // From here Australia
            "Eastern Australia",
            "Indonesia",
            "New Guinea",
            "Western Australia", // Index = 9

            // From here North America
            "Alaska",
            "Alberta (Western Canada)",
            "Central America",
            "Eastern United States",
            "Greenland",
            "Northwest Territory",
            "Ontario (Central Canada)",
            "Quebec (Eastern Canada)",
            "Western United States", // Index = 18

            // From here South America
            "Argentina",
            "Brazil",
            "Peru",
            "Venezuela", // Index = 22

            // From here Europe
            "Great Britain (Great Britain & Ireland)",
            "Iceland",
            "Northern Europe",
            "Scandinavia",
            "Southern Europe",
            "Ukraine (Eastern Europe, Russia)",
            "Western Europe", // Index = 29

            // From here Asia
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
            "Yakutsk" // Index = 41

    };

    public static final HashMap<String, Integer> continentIndicesToTroops = new HashMap<>();
    static {
        continentIndicesToTroops.put("0-5", 3);
        continentIndicesToTroops.put("6-9", 2);
        continentIndicesToTroops.put("10-18", 5);
        continentIndicesToTroops.put("19-22", 2);
        continentIndicesToTroops.put("23-29", 5);
        continentIndicesToTroops.put("30-41", 7);
    }

    /**
     * The edges between the countries (Represents a connection from country to country)
     * E.g. Alaska <-> Northwest Territory
     * This means that, if an edge exists between them, troops of a country can invade another country
     */
    public static final String[][] countryEdges = {
            //Edges within North America
            {"Alaska", "Northwest Territory"},
            {"Alaska", "Alberta (Western Canada)"},
            {"Northwest Territory", "Alberta (Western Canada)"},
            {"Northwest Territory", "Greenland"},
            {"Alberta (Western Canada)","Western United States"},
            {"Alberta (Western Canada)","Ontario (Central Canada)"},
            {"Ontario (Central Canada)", "Greenland"},
            {"Ontario (Central Canada)", "Eastern United States" },
            {"Eastern United States", "Western United States"},
            {"Eastern United States", "Quebec (Eastern Canada)"},
            {"Quebec (Eastern Canada)", "Greenland"},
            {"Western United States", "Central America" },
            {"Western United States", "Ontario (Central Canada)"},

            //Edges within South America
            {"Venezuela", "Brazil"},
            {"Venezuela", "Peru"},
            {"Peru", "Argentina"},
            {"Brazil", "Argentina"},

            //Edges within Africa
            {"North Africa", "Egypt"},
            {"North Africa", "Congo (Central Africa)"},
            {"North Africa", "East Africa"},
            {"Egypt", "East Africa" },
            {"Congo (Central Africa)", "East Africa" },
            {"Congo (Central Africa)", "South Africa"},
            {"East Africa", "Madagascar"},
            {"South Africa","Madagascar"},

            //Edges within Europe
            {"Iceland","Great Britain (Great Britain & Ireland)" },
            {"Iceland", "Scandinavia"},
            {"Great Britain (Great Britain & Ireland)", "Scandinavia"},
            {"Scandinavia", "Northern Europe"},
            {"Scandinavia", "Ukraine (Eastern Europe, Russia)" },
            {"Great Britain (Great Britain & Ireland)", "Northern Europe" },
            {"Great Britain (Great Britain & Ireland)", "Western Europe"},
            {"Western Europe", "Northern Europe"},
            {"Western Europe", "Southern Europe"},
            {"Northern Europe", "Ukraine (Eastern Europe, Russia)"},
            {"Northern Europe", "Southern Europe"},
            {"Southern Europe", "Ukraine (Eastern Europe, Russia)"},

            //Edges within Asia
            {"Middle East","Afghanistan"},
            {"Middle East", "India (Hindustan)"},
            {"Afghanistan", "Ural"},
            {"Afghanistan", "India (Hindustan)"},
            {"Ural","Siberia"},
            {"India (Hindustan)","China"},
            {"Ural", "China"},
            {"Siberia", "China"},
            {"Siberia", "Mongolia"},
            {"Siberia", "Irkutsk"},
            {"Siberia", "Yakutsk"},
            {"Irkutsk", "Yakutsk"},
            {"Irkutsk", "Mongolia"},
            {"Mongolia", "China"},
            {"China", "Siam (Southeast Asia)"},
            {"Mongolia", "Japan"},
            {"Irkutsk", "Kamchatka"},
            {"Yakutsk", "Kamchatka"},
            {"Kamchatka", "Japan"},
            {"India (Hindustan)", "Siam (Southeast Asia)"},
            {"Afghanistan", "China"},
            {"Mongolia", "Kamchatka"},

            //Edges within Australasia
            {"Indonesia", "New Guinea"},
            {"Indonesia", "Western Australia"},
            {"Western Australia", "New Guinea"},
            {"Western Australia", "Eastern Australia"},
            {"Eastern Australia", "New Guinea"},

            //Edges between continents are here

            //North America to
            {"Alaska", "Kamchatka"}, // Asia
            {"Central America", "Venezuela"},// South America
            {"Greenland", "Iceland"}, // Europe

            {"Brazil", "North Africa"}, //South America to Africa

            //Africa to
            {"North Africa", "Western Europe"},//Europe
            {"North Africa", "Southern Europe"},
            {"Egypt", "Southern Europe"},
            {"Egypt", "Middle East"},//Asia
            {"East Africa", "Middle East"},

            //Europe to
            {"Ukraine (Eastern Europe, Russia)", "Middle East"},//Asia
            {"Ukraine (Eastern Europe, Russia)", "Afghanistan"},
            {"Ukraine (Eastern Europe, Russia)", "Ural"},
            {"Southern Europe", "Middle East"},

            //Asia to
            {"Siam (Southeast Asia)", "Indonesia"} //Australasia
    };

}
