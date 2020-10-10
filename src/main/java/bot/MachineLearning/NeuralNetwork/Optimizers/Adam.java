package MachineLearning.NeuralNetwork.Optimizers;

import MachineLearning.NeuralNetwork.Layers.Layer;
import MachineLearning.NeuralNetwork.Model;
import bot.Mathematics.LinearAlgebra.Matrix;
import bot.Mathematics.LinearAlgebra.Vector;

import java.util.List;

public class Adam extends Optimizer {

    private final double beta1;
    private final double beta2;
    private final double epsilon;

    private Matrix[] sWeights;
    private Vector[] sBias;

    private Matrix[] mWeights;
    private Vector[] mBias;

    private int curEpoch;

    public Adam(double learningRate, double beta1, double beta2, double epsilon) {
        super(learningRate, 0);
        this.beta1 = beta1;
        this.beta2 = beta2;
        this.epsilon = epsilon;
    }

    public Adam(double learningRate, double beta1, double beta2) {
        super(learningRate, 0);
        this.beta1 = beta1;
        this.beta2 = beta2;
        this.epsilon = 10e-7;
    }

    @Override
    public void updateWeights(Model model) {
        List<Layer> layers = model.getLayers();
        for (int i = 0; i < layers.size(); i++) {
            Layer l = layers.get(i);

            Matrix curSWeight = sWeights[i];
            Vector curSBias = sBias[i];

            Matrix curMWeight = mWeights[i];
            Vector curMBias = mBias[i];

            Matrix weightGradients = l.getGradientSumWeight();
            Vector biasGradients = l.getGradientSumBias();

            updateMomentum(weightGradients, biasGradients, curMWeight, curMBias);
            updateSquaredMomentum(weightGradients, biasGradients, curSWeight, curSBias);

            Matrix mDwCorrected = curMWeight.getScaled(1/(1+Math.pow(beta1, curEpoch)));
            Vector mDbCorrected = curMBias.getScaled(1/(1+Math.pow(beta1, curEpoch)));
            Matrix sDwCorrected = curSWeight.getScaled(1/(1+Math.pow(beta2, curEpoch)));
            Vector sDbCorrected = curSBias.getScaled(1/(1+Math.pow(beta2, curEpoch)));

            Matrix updateToWeights = sDwCorrected.getExponentScaled(0.5);
            updateToWeights.add(epsilon);
            updateToWeights.exponentScale(-1);
            updateToWeights.elementMultiply(mDwCorrected);
            updateToWeights.scale(-1*learningRate);

            Vector updateToBias = sDbCorrected.getExponentScaled(0.5);
            updateToBias.add(epsilon);
            updateToBias.exponentScale(-1);
            updateToBias.multiply(mDbCorrected);
            updateToBias.scale(-1*learningRate);

            l.getRepresentation().add(updateToWeights);
            l.getBias().add(updateToBias);

        }
    }

    private void updateMomentum(Matrix weightGrads, Vector biasGrads, Matrix mW, Vector mB) {
        mW.scale(beta1);
        mW.add(weightGrads.getScaled(1-beta1));
        mB.scale(beta1);
        mB.add(biasGrads);
    }


    private void updateSquaredMomentum(Matrix weightGrads, Vector biasGrads, Matrix mW, Vector mB) {
        mW.scale(beta2);
        mW.add(weightGrads.getElementMultiplied(weightGrads).getScaled(1-beta2));
        mB.scale(beta2);
        mB.add(biasGrads.getMultiplied(biasGrads).getScaled(1-beta2));
    }

    @Override
    public void init(Model model) {
        List<Layer> layers = model.getLayers();
        int numLayers = layers.size();

        sWeights = new Matrix[numLayers];
        sBias = new Vector[numLayers];

        mWeights = new Matrix[numLayers];
        mBias = new Vector[numLayers];

        for (int i = 0; i < numLayers; i++) {
            Layer curLayer = layers.get(i);
            int[] curWeightsDims = curLayer.getRepresentation().getSize();
            int curBiasDims = curLayer.getBias().getDimensions();

            sWeights[i] = new Matrix(curWeightsDims);
            sBias[i] = new Vector(curBiasDims);

            mWeights[i] = new Matrix(curWeightsDims);
            mBias[i] = new Vector(curBiasDims);

        }

        curEpoch = 0;

    }

}
