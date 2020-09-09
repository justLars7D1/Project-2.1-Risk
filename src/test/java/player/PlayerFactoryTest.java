package player;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerFactoryTest {
    @Test
    public void testJUnitSetup() {
        assertTrue(true);
    }

    @Test
    public void testCreateHumanPlayer(){
        Player user = PlayerFactory.createHumanPlayer();
        assertNotNull(user);
    }
}