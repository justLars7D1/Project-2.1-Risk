package bot.MachineLearning.NeuralNetwork.Losses;

import bot.Mathematics.LinearAlgebra.Vector;

public class DoubleQLoss implements Loss {

    /* Double Q-Loss Hasselt (2015)
    *
    * Y = R[t+1] + gamma * Q'(S[t+1], argmax(a) Q(S[t+1], a)) 
    *
    * estimates loss based on the target network and future reward.
    */
    @Override
    // needs to take state-action pairs Q(S,a) and rewards R[t+1]
    public Vector evaluate(Vector yActual, Vector yPred) {

    }

    @Override
    public Vector evalDerivative(Vector yPred, Vector yActual) {
    }
}
