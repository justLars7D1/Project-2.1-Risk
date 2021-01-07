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

        Double[] winChanceIncrement = new Double[]{0.0,10.0,1.0};
        Double[] randomChanceIncrement = new Double[]{0.0,10.0,1.0};
        Double[] alphaIncrement = new Double[]{0.0,10.0,1.0};
        Double[] lambdaIncrement = new Double[]{0.0,10.0,1.0};
        HyperParameterTrain(1,2,true, winChanceIncrement, randomChanceIncrement, alphaIncrement, lambdaIncrement);
    }

    public static void HyperParameterTrain(int numGamesPerIteration, int turnsPerGame, boolean verbose,  Double[] winChanceIncrement, Double[] randomChanceIncrement, Double[] alphaIncrement, Double[] lamdaIncrement){
        GameEnvironment environment = new GameEnvironment(PlayerType.TD);
        environment.setPerGame(true);
        // The experiment bot will always play against the bot using default values
        LinearTDBot ExperimentBot =  (LinearTDBot) environment.game.getAllPlayer().get(0);

        // Creating default values that are used when not incrementing this variable
        double defaultWinChanceThreshold = 0.5;
        double defaultRandomChanceThreshold = 0.2;
        double defaultAlpha = 0.05;
        double defaultLambda = 0.5;


        // Experiment winChance Increment
        if(winChanceIncrement.length == 3){
            double start = winChanceIncrement[0];
            double numIncrements = winChanceIncrement[1];
            double end = winChanceIncrement[2];

            if(start < end){
                double increment = (end - start) / numIncrements;

                ExperimentBot.setWinChanceThreshold(start);
                // Set default that are not changed in this run
                ExperimentBot.setrandomChanceThreshold(defaultRandomChanceThreshold);
                ExperimentBot.setLambda(defaultLambda);
                ExperimentBot.setAlpha(defaultAlpha);


                // starting at -1 as there are numIncrements +1 run as there is one initial run with the start value
                for(int i = -1; i < numIncrements; i++){
                    System.out.println("+++++++++++++++ Iteration "+i+" +++++++++++++++");
                    System.out.println("+++++ using winChanceThreshold:"+ ExperimentBot.getWinChanceThreshold() +" +++++" );
                    environment.train(numGamesPerIteration, turnsPerGame, verbose);
                    ExperimentBot.setWinChanceThreshold(ExperimentBot.getWinChanceThreshold()+increment);
                }
                System.out.println("++++++++++++++++++ DONE ++++++++++++++++++");
            }
            else{System.out.println("end larger then start winchance");}
        }
        // Experiment randomChance Increment
        if(randomChanceIncrement.length == 3){
            double start = randomChanceIncrement[0];
            double numIncrements = randomChanceIncrement[1];
            double end = randomChanceIncrement[2];

            if(start < end){
                double increment = (end - start) / numIncrements;

                ExperimentBot.setrandomChanceThreshold(start);
                // Set default that are not changed in this run
                ExperimentBot.setWinChanceThreshold(defaultWinChanceThreshold);
                ExperimentBot.setLambda(defaultLambda);
                ExperimentBot.setAlpha(defaultAlpha);

                for(int i = -1; i < numIncrements; i++){
                    System.out.println("+++++++++++++++ Iteration "+i+" +++++++++++++++");
                    System.out.println("+++++ using randomChanceThreshold:"+ ExperimentBot.getrandomChanceThreshold() +" +++++" );
                    environment.train(numGamesPerIteration, turnsPerGame, verbose);
                    ExperimentBot.setrandomChanceThreshold(ExperimentBot.getrandomChanceThreshold()+increment);
                }
            }
            else{System.out.println("Wrong input randomChance");}
        }
        // Experiment Alpha Increment
        if(alphaIncrement.length == 3){
            double start = alphaIncrement[0];
            double numIncrements = alphaIncrement[1];
            double end = alphaIncrement[2];

            if(start < end){
                double increment = (end - start) / numIncrements;

                ExperimentBot.setAlpha(start);
                // Set default that are not changed in this run
                ExperimentBot.setWinChanceThreshold(defaultWinChanceThreshold);
                ExperimentBot.setLambda(defaultLambda);
                ExperimentBot.setrandomChanceThreshold(defaultRandomChanceThreshold);

                for(int i = -1; i < numIncrements; i++){
                    System.out.println("+++++++++++++++ Iteration "+i+" +++++++++++++++");
                    System.out.println("+++++ using alpha:"+ ExperimentBot.getAlpha() +" +++++" );
                    environment.train(numGamesPerIteration, turnsPerGame, verbose);
                    ExperimentBot.setAlpha(ExperimentBot.getAlpha()+increment);
                }
            }
            else{System.out.println("Wrong input randomChance");}
        }
        // Experiment Alpha Increment
        if(lamdaIncrement.length == 3){
            double start = lamdaIncrement[0];
            double numIncrements = lamdaIncrement[1];
            double end = lamdaIncrement[2];

            if(start < end){
                double increment = (end - start) / numIncrements;

                ExperimentBot.setLambda(start);
                // Set default that are not changed in this run
                ExperimentBot.setWinChanceThreshold(defaultWinChanceThreshold);
                ExperimentBot.setAlpha(defaultAlpha);
                ExperimentBot.setrandomChanceThreshold(defaultRandomChanceThreshold);

                for(int i = -1; i < numIncrements; i++){
                    System.out.println("+++++++++++++++ Iteration "+i+" +++++++++++++++");
                    System.out.println("+++++ using lambda:"+ ExperimentBot.getLambda() +" +++++" );
                    environment.train(numGamesPerIteration, turnsPerGame, verbose);
                    ExperimentBot.setLambda(ExperimentBot.getLambda()+increment);
                }
            }
            else{System.out.println("Wrong input randomChance");}
        }

    }
}
