package environment;

import gameelements.player.PlayerType;

public class EnvironmentTest {

    public static void main(String[] args) {
        GameEnvironment environment = new GameEnvironment(PlayerType.DQN);
        System.out.println("--- Distribution ---");
        environment.finishDistributionPhase();
        System.out.println("--- Placement ---");
        environment.finishPlacementPhase();
        System.out.println("--- Attack ---");
        environment.train(2, true);
    }

}
