package bot.MachineLearning.NeuralNetwork.Losses;

import bot.Mathematics.LinearAlgebra.Vector;

public class BCE implements Loss {
    @Override
    public Vector evaluate(Vector yActual, Vector yPred) {
        /*
        * BCE = -t*log(sig(o)) - (1-t)*log(1-sig(o))
        */
        yPred.sigmoid();

        //left-hand side
        Vector posPred = yPred.getLog();
        Vector posTerm = posPred.getMultiplied(yActual).getScaled(-1.0);

        // right-hand side
        Vector negPred = yPred.getScaled(-1.0).getAdded(1.0);
        Vector negActual = yActual.getScaled(-1.0).getAdded(1.0);
        Vector negTerm = negActual.getMultiplied(negPred.getLog());

        return posTerm.getAdded(negTerm);
    }

    @Override
    public Vector evalDerivative(Vector yPred, Vector yActual) {
        
    }
}
