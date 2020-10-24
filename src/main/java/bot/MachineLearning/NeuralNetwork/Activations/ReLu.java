package bot.MachineLearning.NeuralNetwork.Activations;

import bot.Mathematics.LinearAlgebra.Vector;

public class ReLu implements Activation {

    public Vector evaluate(Vector x) {
        Vector result = new Vector(x.getDimensions());
        for (int i = 0; i < x.getDimensions(); i++) {
            if (x.get(i) <= 0) result.set(i, 0);
            else result.set(i, x.get(i));
        }
        return result;
    }

    @Override
    public Vector evalDerivative(Vector x) {
        Vector result = new Vector(x.getDimensions());
        for (int i = 0; i < x.getDimensions(); i++) {
            if (x.get(i) <= 0) result.set(i, 0);
            else result.set(i, 1);
        }
        return result;
    }
}
