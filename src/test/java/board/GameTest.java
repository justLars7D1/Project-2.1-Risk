package board;

import org.junit.Test;
import player.Player;
import player.PlayerFactory;

import java.util.HashMap;

import static org.junit.Assert.assertNotNull;

public class GameTest {

    @Test
    public void testBuildSetup() {
        GameMockup game = new GameMockup();
        HashMap<Integer, Integer> playerConfig = new HashMap<>();
        playerConfig.put(1, 1);
        playerConfig.put(2, 2);
        playerConfig.put(3, 3);
        game.buildSetup(playerConfig);
        System.out.print(game.getProxy().players);
    }

}