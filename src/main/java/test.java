import gameelements.board.Country;
import gameelements.game.Game;
import gameelements.player.Player;
import gameelements.player.PlayerType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class test {
    public static void main(String[] args) {
        HashMap<Integer, PlayerType> players = new HashMap<>();
        players.put(0, PlayerType.USER);
        players.put(1, PlayerType.USER);
        Game game = new Game(players);

        System.out.println(game.getGamePhase());
        System.out.println(game.getBattlePhase());

        System.out.println(game.getCurrentPlayer());


    }
    public static void get_state_features(Game game){
        Player current_player = game.getCurrentPlayer();
        HashSet<Country> owned_countries = current_player.getCountriesOwned();
        int bst;
        double bsr;

        for (Country country : owned_countries) {
            bst = 0;
            bsr = 0;
            int player_troops = country.getNumSoldiers();

            List<Country> neighbor_countries = country.getNeighboringCountries();
            for (Country n_country : neighbor_countries) {
                if (n_country.getOwner() != null && n_country.getOwner() != current_player) {
                    bst += n_country.getNumSoldiers();
                }
            }
            bsr = bst / player_troops;
            System.out.println("BST for country" + country + " equals " +bst);
            System.out.println("BSR for country" + country + " equals " +bsr);
        }
    }

}
