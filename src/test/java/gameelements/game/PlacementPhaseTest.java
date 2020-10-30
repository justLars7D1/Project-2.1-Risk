package gameelements.game;

import gameelements.phases.data.DistributionEventData;
import gameelements.phases.data.PlacementEventData;
import gameelements.player.Player;
import gameelements.player.PlayerType;
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
        HashMap<Integer, PlayerType> players = new HashMap<>();
        players.put(0, PlayerType.USER);
        players.put(1, PlayerType.USER);
        return new Game(players);
    }

}
