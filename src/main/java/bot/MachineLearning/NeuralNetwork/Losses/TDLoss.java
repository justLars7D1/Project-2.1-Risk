package bot.MachineLearning.NeuralNetwork.Losses;

import bot.Mathematics.LinearAlgebra.Vector;
import environment.FeatureSpace;

public class TDLoss implements Loss{
    double lambda; // This is lambda from the TD formula, has to lie between 0 and 1.
    @Override
    public Vector evaluate(Vector yPred, Vector yActual) {
        return yActual;
    }

    @Override
    public Vector evalDerivative(Vector yPred, Vector yActual) {
        Vector result = new Vector(FeatureSpace.features.get(0).length);
        for(int j = 0; j < FeatureSpace.features.get(0).length; j++){
            double scale  = 0;
            for(int i = 0; i < FeatureSpace.round; i++){
                Vector featureResult = yPred.getSubtracted(yActual);

                double regulator = Math.pow(lambda, (FeatureSpace.round - i));
                scale += FeatureSpace.features.get(i)[j] * regulator;
                featureResult = featureResult.getScaled(scale);
                result.set(j,featureResult.get(0));
            }
        }
        return result;
    }
    public void obtainLambda(double lambda){
        this.lambda = lambda;
    }
}
