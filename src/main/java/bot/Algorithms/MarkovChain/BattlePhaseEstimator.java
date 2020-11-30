package bot.Algorithms.MarkovChain;

import java.util.*;

/* 
* Probabilistic model of battles in the RISK game
*/
public class BattlePhaseEstimator {

    /*
    * i: number of attacking troops
    * j: number of defending troops
    */
    private static final double[][] attackerWins = {
        {0.417, 0.754, 0.916, 0.972, 0.99,  0.997, 0.999, 1.0,   1.0,   1.0},
        {0.106, 0.363, 0.656, 0.785, 0.89,  0.934, 0.967, 0.98,  0.99,  0.994},
        {0.027, 0.206, 0.47,  0.642, 0.769, 0.857, 0.91,  0.947, 0.967, 0.981},
        {0.007, 0.091, 0.315, 0.477, 0.638, 0.745, 0.834, 0.888, 0.93,  0.954},
        {0.002, 0.049, 0.206, 0.359, 0.506, 0.638, 0.736, 0.818, 0.873, 0.916},
        {0.0,   0.021, 0.134, 0.253, 0.397, 0.521, 0.64,  0.73,  0.808, 0.861},
        {0.0,   0.011, 0.084, 0.181, 0.297, 0.423, 0.536, 0.643, 0.726, 0.8},
        {0.0,   0.005, 0.054, 0.123, 0.224, 0.329, 0.446, 0.547, 0.646, 0.724},
        {0.0,   0.003, 0.033, 0.086, 0.162, 0.258, 0.357, 0.464, 0.558, 0.65},
        {0.0,   0.001, 0.021, 0.057, 0.118, 0.193, 0.287, 0.38,  0.48,  0.568}
    };

    /*
    * transition matrix pi[i,j,k]
    * i: number of attacking troops in range [1,3]-1
    * j: number of defending troops in range [1,2]-1
    * k: troops lost attacker, both and defender
    */
    private static final double [][][] transitionProbabilities = {
        {//i=1
            {0.583, 0.0, 0.417},
            {0.746, 0.0, 0.254}
        },
        {//i=2
            {0.422, 0.0, 0.578},
            {0.448, 0.324, 0.228}
        },
        {//i=3
            {0.34, 0.0, 0.66},
            //to transient states
            {0.237, 0.504, 0.259}

        }
    };

    private static double[][] cachedExpectedLoss = new double[10][10];

    private static class State{
        public double prob;
        public int attacker;
        public int defender;

        public State(int attacker, int defender, double prob){
            this.prob = prob;
            this.attacker = attacker;
            this.defender = defender;
        }
    }

    /*
    * Chance to win a territory by attacking continuously
    */
    public static double winChance(int against, int with){
        return attackerWins[against-1][with-1];
    }

    /*
    * Expected number of troops lost for a win
    */
    public static double expectedLoss(int against, int with){
        if(!(cachedExpectedLoss[with-1][against-1]>0.0)){
            //generates if value isn't cached
            List<State> cache = new ArrayList<>();
            System.out.println("MARKOV CHAIN");
            //calculate end states from initial states
            cacheEndStatesFor(against, with, 1.0, cache);
            System.out.println("END STATES");
            for(int i=0;i<cache.size();i++){
                State state = cache.get(i);
                System.out.println("{"+state.attacker+","+state.defender+","+state.prob+"}");
            } 
            //calculated the expected loss over the end states
            cachedExpectedLoss[with-1][against-1] = cache.stream()
                    .mapToDouble(s->(with-s.attacker)*s.prob)
                    .sum();
        }
        return cachedExpectedLoss[with-1][against-1];
    }

    private static void cacheEndStatesFor(int against, int with, double prob, List<State> cache){
        if(against<0 || with<0){return;}
        System.out.println("{"+with+","+against+"}");

        //add to the list of end states
        if(against==0 || with==0){
            System.out.println("{"+with+","+against+","+prob+"}"+" ADDED");
            cache.add(new BattlePhaseEstimator.State(with, against, prob));
        }
        else{
            //maximum defender 2, attacker 3 troops
            if(against>=2 && with>=3){
                //attacker looses one troop
                cacheEndStatesFor(against-2, with, prob * transitionProbabilities[2][1][0], cache);
                //both loose one troop
                cacheEndStatesFor(against-1, with-1, prob * transitionProbabilities[2][1][1], cache);
                //defender looses two troops
                cacheEndStatesFor(against-2, with, prob * transitionProbabilities[2][1][2], cache);
            }
            else if(with==1 && against==1){
                //defender looses one
                cacheEndStatesFor(against-1, with, prob * transitionProbabilities[0][0][2], cache);
                //attacker looses one
                cacheEndStatesFor(against, with-1, prob * transitionProbabilities[0][0][0], cache);
            }
            else if(with==2 && against==1){
                //defender looses one
                cacheEndStatesFor(against-1, with, prob * transitionProbabilities[1][0][2], cache);
                //attacker looses one
                cacheEndStatesFor(against, with-1, prob * transitionProbabilities[1][0][0], cache);
            }
            else if(with>=3 && against==1){
                //defender looses one
                cacheEndStatesFor(against-1, with, prob * transitionProbabilities[2][0][2], cache);
                //attacker looses one
                cacheEndStatesFor(against, with-1, prob * transitionProbabilities[2][0][0], cache);
            }
            else if(against>=2 && with==1){
                //defender looses one
                cacheEndStatesFor(against-1, with, prob * transitionProbabilities[0][1][2], cache);
                //attacker looses one
                cacheEndStatesFor(against, with-1, prob * transitionProbabilities[0][1][0], cache);
            }
            else if(against>=2 && with==2){
                //defender looses two
                cacheEndStatesFor(against-2, with, prob * transitionProbabilities[1][1][2], cache);
                //both loose one
                cacheEndStatesFor(against-1, with-1, prob * transitionProbabilities[1][1][1], cache);
                //attacker looses two
                cacheEndStatesFor(against, with-2, prob * transitionProbabilities[1][1][0], cache);
            }
        }
    }
    public static void main(String[] args){
        long startTime = System.nanoTime();
        System.out.println("EXPECTED LOSS: "+BattlePhaseEstimator.expectedLoss(3,3));
        System.out.println("time: "+(double)(System.nanoTime()-startTime)/1_000_000_000.0+"ms");
        startTime = System.nanoTime();
        System.out.println("EXPECTED LOSS: "+BattlePhaseEstimator.expectedLoss(3,3));
        System.out.println("time: "+(double)(System.nanoTime()-startTime)/1_000_000_000.0+"ms");
        System.out.println("EXPECTED LOSS: "+BattlePhaseEstimator.expectedLoss(5,3));
    }
}
