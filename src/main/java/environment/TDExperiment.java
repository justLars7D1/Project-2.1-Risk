package environment;

import gameelements.player.LinearTDBot;
import gameelements.player.PlayerType;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TDExperiment {
    public static void main(String[] args) {
        //GameEnvironment environment = new GameEnvironment(PlayerType.TD);
        //System.out.println("--- Attack ---");
        //environment.train(1, 2, true);

        Double[] winChanceIncrement = new Double[]{};
        Double[] randomChanceIncrement = new Double[]{0.0,10.0,1.0};
        HyperParameterTrain(1,2,true, winChanceIncrement, randomChanceIncrement);
    }

    public static void HyperParameterTrain(int numGamesPerIteration, int turnsPerGame, boolean verbose,  Double[] winChanceIncrement, Double[] randomChanceIncrement){
        GameEnvironment environment = new GameEnvironment(PlayerType.TD);

        // Creating default values that are used when not incrementing this variable
        double defaultWinChanceThreshold = 0.5;
        double defaultRandomChanceThreshold = 0.2;

        // if proper input is given start incrementing
        if(winChanceIncrement.length == 3){
            double start = winChanceIncrement[0];
            double numIncrements = winChanceIncrement[1];
            double end = winChanceIncrement[2];

            if(start < end){
                double increment = (end - start) / numIncrements;

                LinearTDBot.setWinChanceThreshold(start);
                LinearTDBot.setrandomChanceThreshold(defaultRandomChanceThreshold);

                // starting at -1 as there are numIncrements +1 run as there is one initial run with the start value
                for(int i = -1; i < numIncrements; i++){
                    System.out.println("+++++++++++++++ Iteration "+i+" +++++++++++++++");
                    System.out.println("+++++ using winChanceThreshold:"+ LinearTDBot.getWinChanceThreshold() +" +++++" );
                    environment.train(numGamesPerIteration, turnsPerGame, verbose);
                    LinearTDBot.setWinChanceThreshold(LinearTDBot.getWinChanceThreshold()+increment);
                }
                System.out.println("++++++++++++++++++ DONE ++++++++++++++++++");
            }
            else{System.out.println("end larger then start winchance");}
        }
        if(randomChanceIncrement.length == 3){
            double start = (double)Array.get(randomChanceIncrement, 0);
            double numIncrements = (double)Array.get(randomChanceIncrement, 1);
            double end = (double)Array.get(randomChanceIncrement, 2);

            if(start < end){
                double increment = (end - start) / numIncrements;

                LinearTDBot.setrandomChanceThreshold(start);
                LinearTDBot.setWinChanceThreshold(defaultWinChanceThreshold);

                for(int i = -1; i < numIncrements; i++){
                    System.out.println("+++++++++++++++ Iteration "+i+" +++++++++++++++");
                    System.out.println("+++++ using randomChanceThreshold:"+ LinearTDBot.getrandomChanceThreshold() +" +++++" );
                    environment.train(numGamesPerIteration, turnsPerGame, verbose);
                    LinearTDBot.setrandomChanceThreshold(LinearTDBot.getrandomChanceThreshold()+increment);
                }
            }
            else{System.out.println("Wrong input randomChance");}
        }
    }
}
