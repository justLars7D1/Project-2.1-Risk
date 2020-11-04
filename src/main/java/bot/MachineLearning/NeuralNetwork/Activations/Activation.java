package bot.MachineLearning.NeuralNetwork.Activations;

import bot.Mathematics.LinearAlgebra.Vector;

import java.io.Serializable;

public interface Activation extends Serializable {

    Vector evaluate(Vector x);
    Vector evalDerivative(Vector x);

}
