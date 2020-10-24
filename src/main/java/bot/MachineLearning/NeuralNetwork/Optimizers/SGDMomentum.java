package bot.MachineLearning.NeuralNetwork.Optimizers;

import bot.MachineLearning.NeuralNetwork.Layers.Layer;
import bot.MachineLearning.NeuralNetwork.Model;
import bot.Mathematics.LinearAlgebra.Matrix;
import bot.Mathematics.LinearAlgebra.Vector;

import java.util.List;

public class SGDMomentum extends Optimizer {

    private final double beta;

    private Matrix[] mWeights;
    private Vector[] mBias;

    public SGDMomentum(double learningRate, double beta) {
        super(learningRate, 0);
        this.beta = beta;
    }

    @Override
    public void updateWeights(Model model) {
        List<Layer> layers = model.getLayers();
        for (int i = 0; i < layers.size(); i++) {
            Layer l = layers.get(i);

            Matrix curMWeight = mWeights[i];
            Vector curMBias = mBias[i];

            Matrix weightGradients = l.getGradientSumWeight();
            Vector biasGradients = l.getGradientSumBias();

            updateMomentum(weightGradients, biasGradients, curMWeight, curMBias);

            l.getRepresentation().add(curMWeight.getScaled(-1*learningRate));
            l.getBias().add(curMBias.getScaled(-1*learningRate));

        }
    }

    private void updateMomentum(Matrix weightGrads, Vector biasGrads, Matrix mW, Vector mB) {
        mW.scale(beta);
        mW.add(weightGrads.getScaled(1-beta));
        mB.scale(beta);
        mB.add(biasGrads);
    }

    @Override
    public void init(Model model) {
        List<Layer> layers = model.getLayers();
        int numLayers = layers.size();

        mWeights = new Matrix[numLayers];
        mBias = new Vector[numLayers];

        for (int i = 0; i < numLayers; i++) {
            Layer curLayer = layers.get(i);
            int[] curWeightsDims = curLayer.getRepresentation().getSize();
            int curBiasDims = curLayer.getBias().getDimensions();

            mWeights[i] = new Matrix(curWeightsDims);
            mBias[i] = new Vector(curBiasDims);
        }
    }

}
