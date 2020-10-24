package bot.MachineLearning.NeuralNetwork.Activations;

import bot.Mathematics.LinearAlgebra.Vector;

public class Pass implements Activation {
    public Vector evaluate(Vector x) {
        return (Vector) x.clone();
    }

    @Override
    public Vector evalDerivative(Vector x) {
        Vector eval = evaluate(x);
        for (int i = 0; i < eval.getDimensions(); i++) {
            eval.set(i, 1);
        }
        return eval;
    }
}
