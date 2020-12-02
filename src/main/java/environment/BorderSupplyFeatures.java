package environment;

import gameelements.board.Board;
import gameelements.game.Game;
import gameelements.board.Country;
import gameelements.player.Player;
import settings.Settings;

import java.util.*;

public class BorderSupplyFeatures {
    /**
     * Returns a negativ measure of the best / strongest enemy
     * based on army strength and owned territories
     * @return negativ strength value (double)
     */
    public static double getBestEnemyFeature(Game game){
        double BestEnemyFeature;
        double minBestEnemyFeature = 0;
        ArrayList<Player> player_list = game.getAllPlayer();

        for(Player player : player_list){
            // Skip current player
            if (player == game.getCurrentPlayer()){ continue; };

            // Calculate feature value
            BestEnemyFeature = -(getArmiesFeature(game.getGameBoard(), player) + getTerritoriesFeature(player)) / 2;

            // Find Player with highest score
            if(BestEnemyFeature < minBestEnemyFeature){
                minBestEnemyFeature = BestEnemyFeature;
            }
        }
        return minBestEnemyFeature;
    }

    /**
     * Returns for a given board and player the army / total army on the board
     * @return returns double army feature
     */
    public static Double getArmiesFeature(Board board, Player player){
        HashSet<Country> owned_countries = player.getCountriesOwned();

        double sum_player_troops = 0;
        double sum_all_troops = board.getTotalNumArmies();
        double armies_feature;

        for(Country owned_country : owned_countries){
            sum_player_troops+= owned_country.getNumSoldiers();
        }

        armies_feature = sum_player_troops / sum_all_troops;
        return armies_feature;
    }

    /**
     * Returns returns for a given player the proportion of territories owned compared to all territories on the map (42)
     * @return returns double proportion of owned territories
     */
    public static double getTerritoriesFeature(Player player){
        int total_territories = Settings.countries.length;
        return (double) player.getNumCountriesOwned() / total_territories;
    }

    /**
     * Returns the total expected enemy reinforcement
     * @return returns int equal to sum of enemy reinforcement
     */
    public static int getTotalEnemyReinforcement(Game game){
        ArrayList<Player> all_player = game.getAllPlayer();

        int totalEnemyReinforcement = 0;
        for(Player player: all_player){
            if(player == game.getCurrentPlayer()){ continue; }
            totalEnemyReinforcement += game.getNumReinforcement(player);
        }
        return totalEnemyReinforcement;
    }

    /**
     * Returns the avaerage BSR of all countries owned by the current player
     * BST: Sum of enemy units in neighboring countries
     * BSR: BST / armies in current players country
     * @return double variable
     */
    public static double getAverageBSR(Game game){
        Player current_player = game.getCurrentPlayer();
        HashSet<Country> owned_countries = current_player.getCountriesOwned();

        double bst;
        double average_bsr = 0;
        int player_troops;
        List<Country> neighbor_countries;
        Player owner;

        for (Country owned_country : owned_countries) {
            bst = 0;
            player_troops = owned_country.getNumSoldiers();

            neighbor_countries = owned_country.getNeighboringCountries();
            for (Country neighbor_country : neighbor_countries) {
                owner = neighbor_country.getOwner();
                if (owner != null && owner != current_player) {
                    bst += neighbor_country.getNumSoldiers();
                }
            }
            average_bsr += bst/ (double) player_troops;
        }
        // Dividing sum of bsr by amount of countries the player ownes
        average_bsr = average_bsr / owned_countries.size();
        return average_bsr;
    }
}