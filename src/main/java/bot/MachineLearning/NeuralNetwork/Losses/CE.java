package bot.MachineLearning.NeuralNetwork.Losses;

import bot.Mathematics.LinearAlgebra.Vector;

public class CE implements Loss {
    @Override
    public Vector evaluate(Vector yActual, Vector yPred) {
        /*
        * CE = - SUM yk * log(pk)
        */
        Vector logPred = yPred.getLog().getMultiplied(yActual);
        return new Vector(- logPred.getVectorSum());
    }

    @Override
    public Vector evalDerivative(Vector yPred, Vector yActual) {
        /*
        * dCE/doj = - SUM yk / pk
        */
        return new Vector(-yActual.getDivided(yPred).getVectorSum());
    }
    
}
