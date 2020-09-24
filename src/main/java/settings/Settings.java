package settings;

/**
 * Settings class that provides easy access to all game constants used (for easy modification / access)
 */
public class Settings {
    /**
     *  Defines the maximum number of cards that can be held on hand by a player.
     */
    public static final int CARDSALLOWED = 5;

    /**
     * The number of countries that exist within the board
     * Here, the index of the country is it's ID (e.g. Alaska -> ID: 0)
     */
    public static final String[] countries = {
            // From here North America
            "Alaska",
            "Alberta (Western Canada)",
            "Central America",
            "Eastern United States",
            "Greenland",
            "Northwest Territory",
            "Ontario (Central Canada)",
            "Quebec (Eastern Canada)",
            "Western United States",
            "Great Britain (Great Britain & Ireland)",

            // From here Europe
            "Iceland",
            "Northern Europe",
            "Scandinavia",
            "Southern Europe",
            "Ukraine (Eastern Europe, Russia)",
            "Western Europe",

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
            "Yakutsk",
            "Argentina",

            // From here South America
            "Brazil",
            "Peru",
            "Venezuela",
            "Congo (Central Africa)",

            // From here Africa
            "East Africa",
            "Egypt",
            "Madagascar",
            "North Africa",
            "South Africa",
            "Eastern Australia",

            // From here Australia
            "Indonesia",
            "New Guinea",
            "Western Australia"};

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
