package bot.MachineLearning.NeuralNetwork.Optimizers;

import bot.MachineLearning.NeuralNetwork.Layers.Layer;
import bot.MachineLearning.NeuralNetwork.Model;
import bot.Mathematics.LinearAlgebra.Matrix;
import bot.Mathematics.LinearAlgebra.Vector;

import java.util.List;

public class RMSProp extends Optimizer {

    private final double beta;
    private final double epsilon;

    private Matrix[] sWeights;
    private Vector[] sBias;

    public RMSProp(double learningRate, double beta, double epsilon) {
        super(learningRate, 0);
        this.beta = beta;
        this.epsilon = epsilon;
    }

    public RMSProp(double learningRate, double beta) {
        super(learningRate, 0);
        this.beta = beta;
        this.epsilon = 10e-7;
    }

    @Override
    public void updateWeights(Model model) {
        List<Layer> layers = model.getLayers();
        for (int i = 0; i < layers.size(); i++) {
            Layer l = layers.get(i);

            Matrix curSWeight = sWeights[i];
            Vector curSBias = sBias[i];

            Matrix weightGradients = l.getGradientSumWeight();
            Vector biasGradients = l.getGradientSumBias();

            updateSquaredMomentum(weightGradients, biasGradients, curSWeight, curSBias);

            Matrix updateToWeights = curSWeight.getExponentScaled(0.5);
            updateToWeights.add(epsilon);
            updateToWeights.exponentScale(-1);
            updateToWeights.elementMultiply(weightGradients);
            updateToWeights.scale(-1*learningRate);

            Vector updateToBias = curSBias.getExponentScaled(0.5);
            updateToBias.add(epsilon);
            updateToBias.exponentScale(-1);
            updateToBias.multiply(biasGradients);
            updateToBias.scale(-1*learningRate);

            l.getRepresentation().add(updateToWeights);
            l.getBias().add(updateToBias);

        }
    }

    private void updateSquaredMomentum(Matrix weightGrads, Vector biasGrads, Matrix mW, Vector mB) {
        mW.scale(beta);
        mW.add(weightGrads.getElementMultiplied(weightGrads).getScaled(1-beta));
        mB.scale(beta);
        mB.add(biasGrads.getMultiplied(biasGrads).getScaled(1-beta));
    }

    @Override
    public void init(Model model) {
        List<Layer> layers = model.getLayers();
        int numLayers = layers.size();

        sWeights = new Matrix[numLayers];
        sBias = new Vector[numLayers];

        for (int i = 0; i < numLayers; i++) {
            Layer curLayer = layers.get(i);
            int[] curWeightsDims = curLayer.getRepresentation().getSize();
            int curBiasDims = curLayer.getBias().getDimensions();

            sWeights[i] = new Matrix(curWeightsDims);
            sBias[i] = new Vector(curBiasDims);
        }
    }

}
