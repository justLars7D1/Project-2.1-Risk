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
        HashMap<Integer, String> playerConfig = new HashMap<>();
        playerConfig.put(0, "user");
        playerConfig.put(1, "bot");
        playerConfig.put(2, "user");
        game.buildSetup(playerConfig);
        System.out.print(game.getProxy().players);
    }

}