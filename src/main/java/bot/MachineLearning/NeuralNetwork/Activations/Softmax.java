package bot.MachineLearning.NeuralNetwork.Activations;

import bot.Mathematics.LinearAlgebra.Vector;

public class Softmax implements Activation {
    public Vector evaluate(Vector x) {

        Vector result = new Vector(x.getDimensions());

        double denominator = 0;
        for (double val: x.getCoordinates()) {
            denominator += Math.exp(val);
        }

        for (int i = 0; i < result.getDimensions(); i++) {
            double value = x.get(i);
            result.set(i, Math.exp(value) / denominator);
        }

        return result;
    }

    @Override
    public Vector evalDerivative(Vector x) {
        return null;
    }

    public static void main(String[] args) {
        Softmax test = new Softmax();
        System.out.println(test.evaluate(new Vector(3, 4, 1)));
    }

}
