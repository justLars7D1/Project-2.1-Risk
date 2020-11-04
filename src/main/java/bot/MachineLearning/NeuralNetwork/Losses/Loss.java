package bot.MachineLearning.NeuralNetwork.Losses;

import bot.Mathematics.LinearAlgebra.Vector;

import java.io.Serializable;

public interface Loss extends Serializable {

    Vector evaluate(Vector yPred, Vector yActual);

    Vector evalDerivative(Vector yPred, Vector yActual);


}
