package player;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerFactoryTest {

    @Test
    public void testCreateHumanPlayer(){
        Player user = PlayerFactory.createHumanPlayer(0);
        assertNotNull(user);
    }
}