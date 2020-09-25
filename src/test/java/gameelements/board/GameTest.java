package gameelements.board;

import gameelements.game.Game;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertNotNull;

public class GameTest {

    @Test
    public void testBuildSetup() {
        HashMap<Integer, Integer> playerConfig = new HashMap<>();
        playerConfig.put(1, 1);
        playerConfig.put(2, 2);
        playerConfig.put(3, 3);
        Game game = new Game(playerConfig);
        //System.out.print(game.getProxy().players);
    }

}