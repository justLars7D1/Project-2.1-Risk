package environment;

import gameelements.player.PlayerType;

public class EnvironmentTest {

    public static void main(String[] args) {
        GameEnvironment environment = new GameEnvironment(PlayerType.DQN);
        System.out.println("--- Attack ---");
        environment.train(10, 1000, true);
    }

}
