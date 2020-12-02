package bot.MachineLearning.NeuralNetwork;

import bot.MachineLearning.NeuralNetwork.Activations.Activation;
import bot.MachineLearning.NeuralNetwork.Layers.Layer;
import bot.MachineLearning.NeuralNetwork.Losses.Loss;
import bot.MachineLearning.NeuralNetwork.Optimizers.Optimizer;
import bot.Mathematics.LinearAlgebra.Matrix;
import bot.Mathematics.LinearAlgebra.Vector;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Model implements Serializable {

    private static final double L2REGULARIZATIONFACTOR = 0;

    private final int inputSize;
    protected List<Layer> layers;

    private Loss lossFunction;
    private Optimizer optimizer;
    private String[] metrics;

    public Model(int inputSize) {
        assert (inputSize > 0);
        this.inputSize = inputSize;
        this.layers = new ArrayList<>();
    }

    public void compile(Loss lossFunction, Optimizer optimizer) {
        assert (lossFunction != null) && (optimizer != null) && (metrics != null);
        this.lossFunction = lossFunction;
        this.optimizer = optimizer;
        this.metrics = metrics;
    }

    public void gradientChecking(Vector x, Vector y) {
        this.forward(x);
        this.computeGradients(x, y);

        System.out.println("-------- GRADIENT CHECKING --------");

        double EPSILON = 0.0001;
        for (int k = 0; k < layers.size(); k++) {
            System.out.println("Layer " + k + ":\n");
            Layer l = layers.get(k);
            Matrix weights = l.getRepresentation();
            Vector bias = l.getBias();
            Matrix computedGradients = l.getGradientSumWeight();

            System.out.println("Backpropagation algorithm:");
            System.out.print("Weights: " + computedGradients);
            System.out.println("Bias: " + l.getGradientSumBias());

            System.out.println("\nNumerically computed gradient:");

            System.out.print("Weights: ");
            int[] size = weights.getSize();
            for (int i = 0; i < size[0]; i++) {
                for (int j = 0; j < size[1]; j++) {
                    double oldVal = weights.get(i, j);
                    weights.set(i, j, oldVal - EPSILON);
                    double res1 = lossFunction.evaluate(evaluate(x), y).getVectorSum();
                    weights.set(i, j, oldVal + EPSILON);
                    double res2 = lossFunction.evaluate(evaluate(x), y).getVectorSum();
                    weights.set(i, j, oldVal);
                    System.out.print((res2-res1)/(2*EPSILON) + " ");
                }
            }

            System.out.print("\nBias: ");
            int dims = bias.getDimensions();
            for (int i = 0; i < dims; i++) {
                double oldVal = bias.get(i);
                bias.set(i, oldVal - EPSILON);
                double res1 = lossFunction.evaluate(evaluate(x), y).getVectorSum();
                bias.set(i, oldVal + EPSILON);
                double res2 = lossFunction.evaluate(evaluate(x), y).getVectorSum();
                bias.set(i, oldVal);
                System.out.print((res2-res1)/(2*EPSILON) + " ");
            }

            System.out.println("\n");

        }

        System.out.println("-------- GRADIENT CHECKING --------");

        this.resetGradients();
    }

    public void train(Vector[] xs, Vector[] ys, int batchSize, int epochs, int verbose) {
        assert xs.length == ys.length;
        if (lossFunction == null || optimizer == null || metrics == null) {
            try {
                throw new UncompiledModelException();
            } catch (UncompiledModelException e) {
                return;
            }
        }

        optimizer.init(this);

        TrainingBatch[] batches = TrainingBatch.generateUniformlyRandomBatches(xs, ys, batchSize);
        for (int i = 0; i < epochs; i++) {
            for (TrainingBatch batch: batches) {
                Vector[] batchXs = batch.getXs();
                Vector[] batchYs = batch.getYs();
                for (int j = 0; j < batch.getSize(); j++) {
                    // Do a forward pass, gathering all the data we will need
                    this.forward(batchXs[j]);

                    // Compute the gradients and add them to the current gradient matrix
                    this.computeGradients(batchXs[j], batchYs[j]);
                }
                this.averageGradients(batchSize);
                // Now update the weights using the optimizer
                this.optimizer.updateWeights(this);
                this.resetGradients();
            }
            if (verbose >= 1) {
                // Iterate and print the metrics
                System.out.print("Epoch " + (i+1) + "/" + epochs);
                System.out.println();
            }
        }
    }

    private static final double CLASSIFICATION_THRESHOLD = 0.5;
    private double calculateMetricValue(String metric, Vector[] xs, Vector[] ys) {
        double val = 0;
        if (metric.toLowerCase().equals(MetricCollector.EXISTING_METRICS[0])) {
            for (int i = 0; i < xs.length; i++) {
                val += lossFunction.evaluate(evaluate(xs[i]), ys[i]).getVectorSum();
            }
        } else if (metric.toLowerCase().equals(MetricCollector.EXISTING_METRICS[1]) ||
                   metric.toLowerCase().equals(MetricCollector.EXISTING_METRICS[2])) {
            int numCorrect = 0;
            sampleLoop: for (int i = 0; i < xs.length; i++) {
                Vector yPred = evaluate(xs[i]);
                for (int j = 0; j < yPred.getDimensions(); j++) {
                    if ((yPred.get(j) >= CLASSIFICATION_THRESHOLD && ys[i].get(j) < CLASSIFICATION_THRESHOLD)
                        || (yPred.get(j) < CLASSIFICATION_THRESHOLD && ys[i].get(j) >= CLASSIFICATION_THRESHOLD)) {
                        continue sampleLoop;
                    }
                }
                numCorrect++;
            }
            val = ((double) numCorrect) / xs.length;
        }
        return val;
    }

    public void computeGradients(Vector x, Vector y) {
        Layer layerBefore = null;
        Vector layerErrorBefore = null;
        for (int i = layers.size()-1; i >= 0; i--) {
            Layer currentLayer = layers.get(i);

            Vector dJda;
            if (layerBefore != null) {
                Matrix layerWeights = layerBefore.getRepresentation();
                // Calculate dJ/da, which equals the weight matrix (da/dz) times dJ/da (that was previously computed)
                dJda = layerWeights.getTransposed().multiply(layerErrorBefore);
            } else {
                // Means we're at the final layer of the model right now
                // First calculate dJ/da, where J is the loss function and a is the predicted y-value
                dJda = lossFunction.evalDerivative(currentLayer.getOutputAfterActivation(), y);
            }

            // Now calculate da/dz, which is the derivative of the activation function (g'(z))
            Vector dadz = currentLayer.getActivation().evalDerivative(currentLayer.getOutputBeforeActivation());
            Vector layerError = dJda.getMultiplied(dadz); // dJ/da * da/dz = dJ/dz
            Matrix weightError = getLastLayerActivation(i, x).multiply(layerError.getTransposed()); // dJ/dz * dz/dw = dJ/dw
            // Now add the correct errors to the weights and biases of this layer
            currentLayer.getGradientSumBias().add(layerError); // dJ/dz = dJ/db
            currentLayer.getGradientSumWeight().add(weightError.getTransposed());

            layerErrorBefore = layerError;
            layerBefore = currentLayer;
        }
    }

    public void computeGradientsRL(Vector x, Vector y, Vector Y) {
        Layer layerBefore = null;
        Vector layerErrorBefore = null;
        for (int i = layers.size()-1; i >= 0; i--) {
            Layer currentLayer = layers.get(i);

            Vector dJda;
            if (layerBefore != null) {
                Matrix layerWeights = layerBefore.getRepresentation();
                // Calculate dJ/da, which equals the weight matrix (da/dz) times dJ/da (that was previously computed)
                dJda = layerWeights.getTransposed().multiply(layerErrorBefore);
            } else {
                // Means we're at the final layer of the model right now
                // First calculate dJ/da, where J is the loss function and a is the predicted y-value
                dJda = lossFunction.evalDerivative(Y, y);
            }

            // Now calculate da/dz, which is the derivative of the activation function (g'(z))
            Vector dadz = currentLayer.getActivation().evalDerivative(currentLayer.getOutputBeforeActivation());
            Vector layerError = dJda.getMultiplied(dadz); // dJ/da * da/dz = dJ/dz
            Matrix weightError = getLastLayerActivation(i, x).multiply(layerError.getTransposed()); // dJ/dz * dz/dw = dJ/dw
            // Now add the correct errors to the weights and biases of this layer
            currentLayer.getGradientSumBias().add(layerError); // dJ/dz = dJ/db
            currentLayer.getGradientSumWeight().add(weightError.getTransposed());

            layerErrorBefore = layerError;
            layerBefore = currentLayer;
        }
    }

    private Matrix getLastLayerActivation(int i, Vector x) {
        return new Matrix((i == 0) ? x : layers.get(i-1).getOutputAfterActivation());
    }

    private void averageGradients(int batchSize) {
        for (Layer l: layers) {
            l.getGradientSumWeight().scale(1./batchSize);
            l.getGradientSumBias().scale(1./batchSize);
        }
    }

    public void resetGradients() {
        for (Layer l: layers) {
            l.resetGradients();
        }
    }

    private void forward(Vector x) {
        Vector currentOutput = (Vector) x.clone();
        for (Layer l: layers) {
            currentOutput = l.evaluateTraining(currentOutput);
        }
    }

    public Vector forwardEvaluate(Vector x) {
        Vector currentOutput = (Vector) x.clone();
        for (Layer l: layers) {
            currentOutput = l.evaluateTraining(currentOutput);
        }

        return layers.get(layers.size() - 1).getOutputAfterActivation();
    }

    public List<Vector> evaluate(List<Vector> input) {
        List<Vector> results = new ArrayList<>();
        for (Vector v: input) {
            results.add(evaluate(v));
        }
        return results;
    }

    public Vector evaluate(Vector input) {
        assert input.getDimensions() == inputSize;
        Vector currentOutput = (Vector) input.clone();
        for (Layer l: layers) {
            currentOutput = l.evaluate(currentOutput);
        }
        return currentOutput;
    }

    public void addLayer(int numNeurons, Activation activation) {
        int lastOutputSize = (layers.size() > 0) ? layers.get(layers.size()-1).getOutputSize() : inputSize;
        layers.add(new Layer(lastOutputSize, numNeurons, activation));
    }

    public boolean save(String fileAndPath) {
        try {
            FileOutputStream fileOut = new FileOutputStream(fileAndPath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Model loadModel(String fileAndPath) {
        try {
            FileInputStream fileIn = new FileInputStream(fileAndPath);
            ObjectInputStream objectOut = new ObjectInputStream(fileIn);
            return (Model) objectOut.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

        @Override
    public String toString() {
        StringBuilder res = new StringBuilder("Model Overview\n");
        int idCounter = 0;
        for (Layer l : layers) {
            res.append(l.toString(idCounter++)).append("\n");
        }
        res.append("---------------------");
        return res.toString();
    }

    public List<Layer> getLayers() {
        return layers;
    }

    static class UncompiledModelException extends Exception {
    }

}