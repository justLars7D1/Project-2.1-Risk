package bot.MachineLearning.NeuralNetwork.Activations;

import bot.Mathematics.LinearAlgebra.Vector;

public class LeakyReLu implements Activation {

    private final double negative_coefficient;

    public LeakyReLu(double negative_coefficient) {
        this.negative_coefficient = negative_coefficient;
    }

    public LeakyReLu() {
        this(0.1);
    }


    public Vector evaluate(Vector x) {
        Vector result = new Vector(x.getDimensions());
        for (int i = 0; i < x.getDimensions(); i++) {
            double xVal = x.get(i);
            if (xVal <= 0) result.set(i, xVal*negative_coefficient);
            else result.set(i, xVal);
        }
        return result;
    }

    @Override
    public Vector evalDerivative(Vector x) {
        Vector result = new Vector(x.getDimensions());
        for (int i = 0; i < x.getDimensions(); i++) {
            if (x.get(i) <= 0) result.set(i, negative_coefficient);
            else result.set(i, 1);
        }
        return result;
    }
}
