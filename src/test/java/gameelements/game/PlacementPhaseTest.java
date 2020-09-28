package gameelements.game;

import gameelements.board.Board;
import gameelements.board.Country;
import gameelements.phases.data.DistributionEventData;
import gameelements.phases.data.PlacementEventData;
import gameelements.player.Player;
import org.junit.Test;
import settings.Settings;

import java.util.HashMap;

public class PlacementPhaseTest {

    @Test
    public void testPlacementPhase() {
        int numTroops = 1;

        Game game = setupGame();
        for (int i = 0; i < 80; i++) {
            DistributionEventData data = new DistributionEventData(i % Settings.countries.length);
            game.onGameEvent(data);
        }
        System.out.println(game.getGamePhase());

        Player curPlayer = game.getCurrentPlayer();
        System.out.println("Current player: " + curPlayer);

        // Depends on the first player, so won't always be correct result. Seems to work though
        PlacementEventData data = new PlacementEventData(0, numTroops);
        game.onGameEvent(data);

        System.out.println(game.getGameBoard().getCountryFromID(0));

    }

    private Game setupGame() {
        HashMap<Integer, Integer> players = new HashMap<>();
        players.put(0, 1);
        players.put(1, 1);
        return new Game(players);
    }

}
