package environment;

import bot.MachineLearning.NeuralNetwork.Model;
import bot.Mathematics.LinearAlgebra.Vector;
import gameelements.game.Game;
import gameelements.player.PlayerType;

import java.util.HashMap;

public class EnvironmentTest {

    public static void main(String[] args) {
        GameEnvironment environment = new GameEnvironment(PlayerType.DQN, true);
        //environment.train(2500, 1000, true);

        Model m = Model.loadModel("D:\\Projects\\Project-2.1---Game\\src\\main\\java\\gameelements\\player\\player 0-estimator.txt");

//        for (int i = 2; i <= 10; i++) {
//            for (int j = 2; j <= 10; j++) {
//                Vector ans = m.evaluate(new Vector((i - 1)/9., (j - 1)/9.));
//                System.out.println("From: " + i + ", To: " + j + ", Attack: " + (ans.get(1) > ans.get(0)));
//            }
//        }
    }

}
