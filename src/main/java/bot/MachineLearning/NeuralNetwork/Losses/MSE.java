package MachineLearning.NeuralNetwork.Losses;

import bot.Mathematics.LinearAlgebra.Vector;

public class MSE implements Loss {
    @Override
    public Vector evaluate(Vector yActual, Vector yPred) {
        return yActual.getSubtracted(yPred).getExponentScaled(2).getScaled(0.5);
    }

    @Override
    public Vector evalDerivative(Vector yPred, Vector yActual) {
        return yPred.getSubtracted(yActual);
    }
}
