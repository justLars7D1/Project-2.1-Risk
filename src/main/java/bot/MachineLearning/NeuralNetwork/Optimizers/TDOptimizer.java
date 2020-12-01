package bot.MachineLearning.NeuralNetwork.Optimizers;

import bot.MachineLearning.NeuralNetwork.Losses.TDLoss;
import bot.MachineLearning.NeuralNetwork.Model;
import bot.Mathematics.LinearAlgebra.Vector;

public class TDOptimizer extends Optimizer {
    public TDOptimizer(double learningRate, double l2factor) {
        super(learningRate, l2factor);
    }

    @Override
    public void updateWeights(Model model) {



    }

    @Override
    public void init(Model model) {

    }
}
