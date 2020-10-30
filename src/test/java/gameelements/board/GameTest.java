package gameelements.board;

import gameelements.game.Game;
import gameelements.player.PlayerType;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertNotNull;

public class GameTest {

    @Test
    public void testBuildSetup() {
        HashMap<Integer, PlayerType> playerConfig = new HashMap<>();
        playerConfig.put(1, PlayerType.USER);
        playerConfig.put(2, PlayerType.TD);
        playerConfig.put(3, PlayerType.DQN);
        Game game = new Game(playerConfig);
        //System.out.print(game.getProxy().players);
    }

}