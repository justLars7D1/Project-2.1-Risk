package bot.MachineLearning.NeuralNetwork.Optimizers;

import bot.MachineLearning.NeuralNetwork.Model;

public abstract class Optimizer {

    protected final double learningRate;
    protected final double l2factor;

    public Optimizer(double learningRate, double l2factor) {
        assert learningRate > 0;
        this.learningRate = learningRate;
        this.l2factor = l2factor;
    }

    public abstract void updateWeights(Model model);

    public abstract void init(Model model);

}
