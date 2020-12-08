package environment;

import gameelements.player.PlayerType;

public class TDExperiment {
    public static void main(String[] args) {
        GameEnvironment environment = new GameEnvironment(PlayerType.TD);
        System.out.println("--- Attack ---");
        environment.train(1, 1000, true);
    }
}
