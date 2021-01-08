package environment;

import gameelements.game.Game;
import gameelements.player.PlayerType;

import java.util.HashMap;

public class EnvironmentTest {

    public static void main(String[] args) {
        GameEnvironment environment = new GameEnvironment(PlayerType.DQN, true);
        environment.train(100, 1000, true);

    }

}
