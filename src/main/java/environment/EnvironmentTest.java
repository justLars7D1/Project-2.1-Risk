package environment;

import gameelements.game.Game;
import gameelements.player.PlayerType;

import java.util.HashMap;

public class EnvironmentTest {

    public static void main(String[] args) {
        GameEnvironment environment = new GameEnvironment(PlayerType.DQN, true);
        environment.train(200, 1000, true);

//        HashMap<Integer, PlayerType> players = new HashMap<>();
//        players.put(0, PlayerType.DQN);
//        players.put(1, PlayerType.DQN);
//
//        Game g1 = new Game(players, 0);
//        Game g2 = new Game(players, 0);
//
//        System.out.println(g1);
//        System.out.println(g2);

    }

}
