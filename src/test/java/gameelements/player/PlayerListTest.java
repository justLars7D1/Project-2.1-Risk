package gameelements.player;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class PlayerListTest {

    @Test
    public void testPlayerIteration(){
        LinkedList<Player> queue = new LinkedList<>(Arrays.asList(PlayerFactory.createPlayer(1, 0),
                                                            PlayerFactory.createPlayer(2, 0),
                                                            PlayerFactory.createPlayer(3, 0)));
        PlayerList playerList = new PlayerList(queue);

        int max = 8;
        for (int i = 0; i < max; i++) {
            Player curPlayer = playerList.getCurrentPlayer();
            playerList.nextPlayer();
            System.out.println(curPlayer);
            assertEquals(i % 3 + 1, curPlayer.getId());
        }

    }

    @Test
    public void testPlayerRemoval(){
        Player p1 = PlayerFactory.createPlayer(1, 0),
                p2 = PlayerFactory.createPlayer(2, 0),
                p3 = PlayerFactory.createPlayer(3, 0);
        LinkedList<Player> queue = new LinkedList<>(Arrays.asList(p1, p2, p3));
        PlayerList playerList = new PlayerList(queue);

        playerList.removePlayer(p2);

        int max = 8;
        for (int i = 0; i < max; i++) {
            Player curPlayer = playerList.getCurrentPlayer();
            playerList.nextPlayer();
            if (i % 2 == 0) assertEquals(1, curPlayer.getId());
            else assertEquals(3, curPlayer.getId());
        }

    }

}