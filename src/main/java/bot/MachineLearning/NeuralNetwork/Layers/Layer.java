package bot.MachineLearning.NeuralNetwork.Layers;

import bot.MachineLearning.NeuralNetwork.Activations.Activation;
import bot.Mathematics.LinearAlgebra.Matrix;
import bot.Mathematics.LinearAlgebra.Vector;

import java.io.Serializable;
import java.util.Random;

public class Layer implements Serializable {

    private Activation activation;
    private Matrix representation;

    private Vector bias;

    // The following variables are used for training:

    private Matrix gradientSumWeight;
    private Vector gradientSumBias;

    private Vector outputBeforeActivation;
    private Vector outputAfterActivation;

    public Layer(int numInputNeurons, int numOutputNeurons, Activation activation) {
        this.activation = activation;
        representation = new Matrix(numOutputNeurons, numInputNeurons);
        gradientSumWeight = new Matrix(numOutputNeurons, numInputNeurons);
        bias = new Vector(numOutputNeurons);
        gradientSumBias = new Vector(numOutputNeurons);
        setRandomWeights();
    }

    public Vector evaluate(Vector input) {
        Vector result = representation.multiply(input);
        result.add(bias);
        result = activation.evaluate(result);
        return result;
    }

    public Vector evaluateTraining(Vector input) {
        outputBeforeActivation = representation.multiply(input); // This will be the value before the activation is applied
        outputBeforeActivation.add(bias);

        Vector copyOfResult = (Vector) outputBeforeActivation.clone();
        copyOfResult = activation.evaluate(copyOfResult);
        outputAfterActivation = copyOfResult; // This will be the value after the activation is applied

        return outputAfterActivation;
    }

    public void resetGradients() {
        gradientSumWeight = new Matrix(gradientSumWeight.getSize());
        gradientSumBias = new Vector(gradientSumBias.getDimensions());
        outputBeforeActivation = new Vector(outputBeforeActivation.getDimensions());
        outputAfterActivation = new Vector(outputAfterActivation.getDimensions());
    }

    public Matrix getRepresentation() {
        return representation;
    }

    public Vector getBias() {
        return bias;
    }

    public int getOutputSize() {
        return representation.getSize()[0];
    }

    private void setRandomWeights() {
        // We will use the Xavier uniform initializer
        Random r = new Random();
        int[] size = representation.getSize();
        double limit = Math.sqrt(6. / (size[0] + size[1]));
        for (int i = 0; i < size[0]; i++) {
            for (int j = 0; j < size[1]; j++) {
                representation.set(i, j, -limit + 2 * limit * r.nextDouble());
            }
        }
    }

    public Matrix getGradientSumWeight() {
        return gradientSumWeight;
    }

    public Vector getGradientSumBias() {
        return gradientSumBias;
    }

    public Vector getOutputBeforeActivation() {
        return outputBeforeActivation;
    }

    public Vector getOutputAfterActivation() {
        return outputAfterActivation;
    }

    public Activation getActivation() {
        return activation;
    }

    public String toString(int id) {
        String res = "---- Layer " + id + ": Dense ----\n";
        res += "Weights: \n";
        res += representation;
        res += "Bias: \n";
        res += bias;
        return res;
    }

    @Override
    public String toString() {
        String res = "---- Dense layer ----\n";
        res += "Weights: \n";
        res += representation;
        res += "Bias: \n";
        res += bias;
        return res;
    }
}
