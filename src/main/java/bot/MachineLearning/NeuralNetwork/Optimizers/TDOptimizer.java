package bot.MachineLearning.NeuralNetwork.Optimizers;

import bot.MachineLearning.NeuralNetwork.Layers.Layer;
import bot.MachineLearning.NeuralNetwork.Losses.TDLoss;
import bot.MachineLearning.NeuralNetwork.Model;
import bot.Mathematics.LinearAlgebra.Matrix;
import bot.Mathematics.LinearAlgebra.Vector;

import java.util.ArrayList;
import java.util.List;

public class TDOptimizer extends Optimizer {
    private Vector losses;

    public TDOptimizer(double learningRate, double l2factor) {
        super(learningRate, l2factor);
    }

    @Override
    public void updateWeights(Model model) {
        Vector weightIncrements = new Vector(losses.getCoordinates().length);
        ArrayList<Layer> layers = (ArrayList<Layer>) model.getLayers();
        Matrix currentWeights = layers.get(0).getRepresentation();
        double[][] weigths = currentWeights.getGrid();
        double[] vectorPrep = weigths[0];
        for (int l = 0; l < vectorPrep.length; l++){
            System.out.println(vectorPrep[l]);
        }
        for(int i = 0; i < losses.getCoordinates().length; i++){
            double newWeight = vectorPrep[i] - (learningRate * losses.get(i));
            double increment = newWeight - vectorPrep[i];
            weightIncrements.set(i, increment);
        }
        Matrix increments = new Matrix(weightIncrements);
        model.getLayers().get(0).getRepresentation().add(increments.getTransposed());

    }

    @Override
    public void init(Model model) {

    }
    public void obtainLosses (Vector losses){
        this.losses = losses;
    }
    public double getLambda() {
        return this.l2factor;
    }
}
