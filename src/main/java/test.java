import gameelements.board.Board;
import gameelements.board.Country;
import gameelements.game.Game;
import gameelements.player.Player;
import gameelements.player.PlayerFactory;
import gameelements.player.PlayerType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class test {
    public static void main(String[] args) {
        HashMap<Integer, PlayerType> players = new HashMap<>();
        players.put(0, PlayerType.USER);
        players.put(1, PlayerType.USER);
        players.put(2, PlayerType.USER);
        Game game = new Game(players);

        // Initialize Test Case
        Player player3 = PlayerFactory.createPlayer(3,20,PlayerType.USER);
        Player player4 = PlayerFactory.createPlayer(4,20,PlayerType.USER);
        Player player5 = PlayerFactory.createPlayer(5,20,PlayerType.USER);

        Board game_board = game.getGameBoard();

        // Get all countries for testcases -> neighbors of east_africa
        Country east_africa = game_board.getCountryFromID(1);
        Country north_africa = game_board.getCountryFromName("North Africa");
        Country egypt = game_board.getCountryFromName("Egypt");
        Country central_africa = game_board.getCountryFromName("Congo (Central Africa)");
        Country madagascar = game_board.getCountryFromName("Madagascar");
        Country south_africa = game_board.getCountryFromName("South Africa");
        Country middle_east = game_board.getCountryFromName("Middle East");


        // Set set the current player as owner east africa
        Player current_player = game.getCurrentPlayer();
        current_player.onDistributionEvent(east_africa);
        east_africa.addNumSoldiers(30);
        current_player.onDistributionEvent(madagascar);
        madagascar.addNumSoldiers(30);

        // Set enemy players
        player3.onDistributionEvent(north_africa);
        north_africa.addNumSoldiers(3);
        player3.onDistributionEvent(egypt);
        egypt.addNumSoldiers(4);
        player3.onDistributionEvent(central_africa);
        central_africa.addNumSoldiers(10);

        player4.onDistributionEvent(south_africa);
        south_africa.addNumSoldiers(22);

        player5.onDistributionEvent(middle_east);
        middle_east.addNumSoldiers(2);

        // Run method
        HashMap <Integer,List<Double>> feature_results =  get_state_features(game);


    }
    public static HashMap get_state_features(Game game){
        Player current_player = game.getCurrentPlayer();
        HashSet<Country> owned_countries = current_player.getCountriesOwned();

        double bst;
        double bsr;
        int player_troops;
        List<Country> neighbor_countries;
        Player owner;
        HashMap<Integer, List<Double>> feature_results = new HashMap<Integer, List<Double>>();
        List<Double> feature_values = new ArrayList<>();

        for (Country owned_country : owned_countries) {
            bst = 0;
            bsr = 0;
            feature_values.clear();
            player_troops = owned_country.getNumSoldiers();

            neighbor_countries = owned_country.getNeighboringCountries();
            for (Country neighbor_country : neighbor_countries) {

                owner = neighbor_country.getOwner();
                if (owner != null && owner != current_player) {
                    bst += neighbor_country.getNumSoldiers();
                }
            }
            bsr = (double) bst/ (double) player_troops;
            System.out.println("BSR:" +bsr);
            System.out.println("BST:" +bst);

            // add to list
            feature_values.add(bst);
            feature_values.add(bsr);
            System.out.println("List containing BSR,BST:"+feature_values);

            // add list to hashmap
            feature_results.put(owned_country.getID(), feature_values);
            System.out.println("HashMap containing feature results: " +feature_results);

        }
        return feature_results;
    }
}
