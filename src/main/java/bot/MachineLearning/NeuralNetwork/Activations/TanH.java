package bot.MachineLearning.NeuralNetwork.Activations;

import bot.Mathematics.LinearAlgebra.Vector;

public class TanH implements Activation {

    public Vector evaluate(Vector x) {
        Vector result = new Vector(x.getDimensions());
        for (int i = 0; i < x.getDimensions(); i++) {
            double val = (Math.exp(x.get(i)) - Math.exp(-x.get(i)))/(Math.exp(x.get(i)) + Math.exp(-x.get(i)));
            result.set(i, val);
        }
        return result;
    }

    @Override
    public Vector evalDerivative(Vector x) {
        Vector eval = evaluate(x);
        for (int i = 0; i < eval.getDimensions(); i++) {
            double value = eval.get(i);
            eval.set(i, 1 - Math.pow(value, 2));
        }
        return eval;
    }
}
