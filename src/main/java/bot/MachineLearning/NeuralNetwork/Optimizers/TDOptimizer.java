package bot.MachineLearning.NeuralNetwork.Optimizers;

import bot.MachineLearning.NeuralNetwork.Layers.Layer;
import bot.MachineLearning.NeuralNetwork.Losses.TDLoss;
import bot.MachineLearning.NeuralNetwork.Model;
import bot.Mathematics.LinearAlgebra.Matrix;
import bot.Mathematics.LinearAlgebra.Vector;
import environment.FeatureSpace;

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
        System.out.println("------------- Weights--------------");
        for (int l = 0; l < vectorPrep.length; l++){
            System.out.println(vectorPrep[l]);
        }
        System.out.println("------------- Weights--------------");
        double normFactor = this.normalization();
        double featureNorm = this.obtainCurrentFeatureNorm();
        for(int i = 0; i < losses.getCoordinates().length; i++){
            //TD with normalized features
            double newWeight = vectorPrep[i] - (learningRate * (losses.get(i)/(normFactor * featureNorm)));

            //TD without normalized features
            //double newWeight = vectorPrep[i] - learningRate * (losses.get(i));

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

    private double normalization(){
        Vector featureRoundSum = new Vector(FeatureSpace.features.get(0).length);
        for(int i = 0; i < FeatureSpace.round; i++){
            double[] roundFeatures = FeatureSpace.features.get(i);
            Vector features = new Vector(roundFeatures.length);
            for(int j = 0; j < roundFeatures.length; j++){
                features.set(j,roundFeatures[j]);
            }
            features.scale(Math.pow(l2factor,FeatureSpace.round - i));
            featureRoundSum.add(features);
        }
        double result = 0;
        for(int k = 0; k < featureRoundSum.getDimensions(); k++) {
            result += Math.pow(featureRoundSum.get(k),2);
        }
        result = Math.sqrt(result);
        return result;
    }
    private double obtainCurrentFeatureNorm(){
        double[] current = FeatureSpace.features.get(FeatureSpace.round-1);
        double sqdSum = 0;
        for(int i = 0; i < current.length; i++){
            sqdSum += Math.pow(current[i],2);
        }
        double result = Math.sqrt(sqdSum);
        return result;
    }
}
