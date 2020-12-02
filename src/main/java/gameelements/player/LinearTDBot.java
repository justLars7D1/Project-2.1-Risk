package gameelements.player;

import bot.MachineLearning.NeuralNetwork.Activations.Pass;
import bot.MachineLearning.NeuralNetwork.Losses.MSE;
import bot.MachineLearning.NeuralNetwork.Model;
import bot.MachineLearning.NeuralNetwork.Optimizers.SGD;
import bot.Mathematics.LinearAlgebra.Vector;
import gameelements.board.Country;
import gameelements.game.Game;

public class LinearTDBot extends RiskBot {

    Model linearEvalFunction;

    private static final int NUM_FEATURES = 2;

    /**
     * algorithm and strategies for our risk bot
     */
    public LinearTDBot(int id, int numTroopsInInventory, Game game) {
        super(id, numTroopsInInventory, game);
        setupModel();
    }

    private void setupModel() {
        this.linearEvalFunction = new Model(NUM_FEATURES);
        this.linearEvalFunction.addLayer(1, new Pass());
        this.linearEvalFunction.compile(new MSE(), new SGD(0.05));
    }

    @Override
    public boolean onDistributionEvent(Country country) {
        // Put all the code to pick the right action here
        return super.onDistributionEvent(country);
    }

    @Override
    public void onPlacementEvent(Country country, int numTroops) {
        // Put all the code to pick the right action here
        super.onPlacementEvent(country, numTroops);
        // Add code for deciding end of event phase here (finish attack phase method)
    }

    @Override
    public void onAttackEvent(Country countryFrom, Country countryTo) {
        // Put all the code to pick the right action here
        double feature1 = 0;
        double feature2 = 1;
        Vector input = new Vector(feature1, feature2);
        Vector ouput = linearEvalFunction.evaluate(input);

        super.onAttackEvent(countryFrom, countryTo);
        // Add code for deciding end of event phase here (finish attack phase method)
    }

    @Override
    public void onFortifyEvent(Country countryFrom, Country countryTo, int numTroops) {
        // Put all the code to pick the right action here
        //super.onFortifyEvent(countryFrom, countryTo, numTroops);
        // Add code for deciding end of event phase here (finish attack phase method)
    }

}
