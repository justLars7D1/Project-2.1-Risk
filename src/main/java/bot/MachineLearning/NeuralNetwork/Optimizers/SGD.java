package bot.MachineLearning.NeuralNetwork.Optimizers;

import bot.MachineLearning.NeuralNetwork.Layers.Layer;
import bot.MachineLearning.NeuralNetwork.Model;
import bot.Mathematics.LinearAlgebra.Matrix;
import bot.Mathematics.LinearAlgebra.Vector;

public class SGD extends Optimizer {

    public SGD(double learningRate, double l2factor) {
        super(learningRate, l2factor);
    }

    public SGD(double learningRate) {
        super(learningRate, 0);
    }

    // w = (1 - lr*lambda)*w - lr * dJ/dw

    @Override
    public void updateWeights(Model model) {
        for (Layer l: model.getLayers()) {

            Matrix weightGradients = l.getGradientSumWeight().getScaled(-1*learningRate);
            Vector biasGradients = l.getGradientSumBias().getScaled(-1*learningRate);

            l.getRepresentation().scale(1 - learningRate*l2factor);

            l.getRepresentation().add(weightGradients);
            l.getBias().add(biasGradients);


        }
    }

    @Override
    public void init(Model model) {
    }

}
