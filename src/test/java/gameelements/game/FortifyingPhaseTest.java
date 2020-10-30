package gameelements.game;

import gameelements.phases.data.DistributionEventData;
import gameelements.phases.data.FortifyEventData;
import gameelements.player.Player;
import gameelements.player.PlayerType;
import org.junit.Test;
import settings.Settings;

import java.util.HashMap;

public class FortifyingPhaseTest {

    @Test
    public void testFortifyingPhase() {
        int numTroops = 1;

        Game game = setupGame();
        for (int i = 0; i < 80; i++) {
            DistributionEventData data = new DistributionEventData(i % Settings.countries.length);
            game.onGameEvent(data);
        }
        game.nextBattlePhase();
        game.nextBattlePhase();

        System.out.println("Phase: " + game.getBattlePhase());

        Player curPlayer = game.getCurrentPlayer();
        System.out.println("Current player: " + curPlayer);

        System.out.println("BEFORE: ");
        System.out.println(game.getGameBoard().getCountryFromID(0));
        System.out.println(game.getGameBoard().getCountryFromID(4));

        // Depends on the first player, so won't always be correct result. Seems to work though
        FortifyEventData data = new FortifyEventData(0, 4, numTroops);
        game.onGameEvent(data);

        System.out.println("AFTER: ");
        System.out.println(game.getGameBoard().getCountryFromID(0));
        System.out.println(game.getGameBoard().getCountryFromID(4));

    }

    @Test
    public void testFortifyingPhaseLongerPath() {
        int numTroops = 1;

        Game game = setupGame();
        for (int i = 0; i < 80; i++) {
            DistributionEventData data = new DistributionEventData(i % Settings.countries.length);
            game.onGameEvent(data);
        }
        game.nextBattlePhase();
        game.nextBattlePhase();

        System.out.println("Phase: " + game.getBattlePhase());

        Player curPlayer = game.getCurrentPlayer();
        System.out.println("Current player: " + curPlayer);

        System.out.println("BEFORE: ");
        System.out.println(game.getGameBoard().getCountryFromID(0));
        System.out.println(game.getGameBoard().getCountryFromID(2));

        // Depends on the first player, so won't always be correct result. Seems to work though
        FortifyEventData data = new FortifyEventData(0, 2, numTroops);
        game.onGameEvent(data);

        System.out.println("AFTER: ");
        System.out.println(game.getGameBoard().getCountryFromID(0));
        System.out.println(game.getGameBoard().getCountryFromID(2));

    }


    private Game setupGame() {
        HashMap<Integer, PlayerType> players = new HashMap<>();
        players.put(0, PlayerType.USER);
        players.put(1, PlayerType.USER);
        return new Game(players);
    }

}
