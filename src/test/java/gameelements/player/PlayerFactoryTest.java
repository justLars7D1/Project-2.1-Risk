package gameelements.player;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerFactoryTest {

    @Test
    public void testCreateHumanPlayer(){
        Player user = PlayerFactory.createPlayer(0, 0, null, PlayerType.USER);
        assertNotNull(user);
    }
}