package settings;

import gameelements.board.Board;
import gameelements.board.Country;
import gameelements.game.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BotSettings {

    public static List<List<Country>> setupDistributionPriorityList(Board board, int numPlayers) {
        List<Country> africa = getCountryInRange(board, 0, 5);
        List<Country> australia = getCountryInRange(board, 6, 9);
        List<Country> northAmerica = getCountryInRange(board, 10, 18);
        List<Country> southAmerica = getCountryInRange(board, 19, 22);
        List<Country> europe = getCountryInRange(board, 23, 29);
        List<Country> asia = getCountryInRange(board, 30, 41);

        List<List<Country>> countryOrder;
        switch (numPlayers) {
            case 2:
                countryOrder = Arrays.asList(northAmerica, australia, southAmerica, africa, europe, asia);
                break;
            case 3:
                countryOrder = Arrays.asList(australia, northAmerica, southAmerica, africa, europe, asia);
                break;
            case 4:
                countryOrder = Arrays.asList(australia, southAmerica, africa, northAmerica, asia, europe);
                break;
            default:
                countryOrder = Arrays.asList(australia, southAmerica, africa, northAmerica, europe, asia);
                break;
        }

        return countryOrder;
    }

    private static List<Country> getCountryInRange(Board board, int min, int max) {
        List<Country> countries = new ArrayList<>();
        for (int i = min; i <= max; i++) countries.add(board.getCountryFromID(i));
        return countries;
    }

}
