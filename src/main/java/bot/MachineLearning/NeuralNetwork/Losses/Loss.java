package MachineLearning.NeuralNetwork.Losses;

import bot.Mathematics.LinearAlgebra.Vector;

public interface Loss {

    Vector evaluate(Vector yPred, Vector yActual);

    Vector evalDerivative(Vector yPred, Vector yActual);


}
