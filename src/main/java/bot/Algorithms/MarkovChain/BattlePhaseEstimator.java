package bot.Algorithms.MarkovChain;

import bot.Mathematics.LinearAlgebra.Matrix;

/* 
* Probabilistic model of battles in the RISK game
*/
public class BattlePhaseEstimator {

    private Matrix attackMatrix;

    public BattlePhaseEstimator(){
        attackMatrix = new Matrix(attackerWins);
    }

    /*
    * i: number of attacking troops
    * j: number of defending troops
    */
    private final double[][] attackerWins = {
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
    private final double [][][] transitionProbabilities = {
        {//i=1
            {0.583, 0.0, 0.417},
            {0.746, 0.0, 0.254}
        },
        {//i=2
            {0.422, 0.0, 0.578},
            {0.373, 0.475, 0.152}
        },
        {//i=3
            {0.341, 0.0, 0.659},
            //to transient states
            {0.237, 0.504, 0.259}
        }
    };

    /*
    * Chance to win a territory by attacking continuously
    */
    public double winChance(int against, int with){
        return attackMatrix.get(against-1, with-1);
    }

    /*
    * Chance to defeat at least a certain number of troops in battle
    */
    public double winChance(int against, int with, int defeatedTroops){
        return (defeatedTroops < against) ? chainProbability(against, with, defeatedTroops, 1.0) : 0.0;
    }

    public double chainProbability(int against, int with, int remainingTroops, double p){
        if(remainingTroops<=0 || against<=0 || with <=0){return 1.0;}
        //going to transient state
        if(against>=2 && with>=3){
            //attacker looses one troop
            p *= transitionProbabilities[2][1][0] * chainProbability(against, with-1, remainingTroops, p)
            //both loose one troop
            + transitionProbabilities[2][1][1] * chainProbability(against-1, with-1, remainingTroops-1, p)
            //defender looses two troops
            + transitionProbabilities[2][1][2] * chainProbability(against-2, with, remainingTroops-2, p);
        }
        //going to absorbant state
        else if(against<=2 && with >=1){
            //probability of the defender losing the last troops
            p *= transitionProbabilities[with-1][against-1][2];
        }
        return p;
    }
}
