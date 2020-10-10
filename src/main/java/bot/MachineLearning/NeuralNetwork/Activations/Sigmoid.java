package MachineLearning.NeuralNetwork.Activations;

import bot.Mathematics.LinearAlgebra.Vector;

public class Sigmoid implements Activation {
    public Vector evaluate(Vector x) {
        Vector result = new Vector(x.getDimensions());
        for (int i = 0; i < x.getDimensions(); i++) {
            result.set(i, 1/(1+Math.exp(-x.get(i))));
        }
        return result;
    }

    @Override
    public Vector evalDerivative(Vector x) {
        Vector eval = evaluate(x);
        for (int i = 0; i < eval.getDimensions(); i++) {
            double value = eval.get(i);
            eval.set(i, value*(1-value));
        }
        return eval;
    }
}
