package gameelements.player;

import gameelements.board.Country;
import gameelements.game.Game;

import bot.MachineLearning.NeuralNetwork.Model;
import bot.MachineLearning.NeuralNetwork.Activations.*;

public class DQNNBot extends RiskBot {
    /*
    * Double Q-Learning (Hasselt, 2010)
    *
    * The weights of the online network are transfered to the
    * target network every n steps.
    *
    * sequential attack decision
    * state = (from, to, threat, ...) -> decision = (from, to, attack)
    *
    * predicts q values for a binary attack decision
    */

    int n;

    Model targetNetwork;

    Model estimatorNetwork;

    /**
     * algorithm and strategies for our risk bot
     */
    public DQNNBot(int id, int numTroopsInInventory, Game game, int numFeatures, int lag) {
        super(id, numTroopsInInventory, game);

        this.n = lag;

        // target approximation
        targetNetwork = new Model(numFeatures);
        targetNetwork.addLayer(3, new ReLu());
        targetNetwork.addLayer(3, new ReLu());
        targetNetwork.addLayer(1, new Sigmoid());

        // dynamic network
        estimatorNetwork = new Model(numFeatures);
        estimatorNetwork.addLayer(3, new ReLu());
        estimatorNetwork.addLayer(3, new ReLu());
        estimatorNetwork.addLayer(1, new Sigmoid());
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
        super.onAttackEvent(countryFrom, countryTo);
        // Add code for deciding end of event phase here (finish attack phase method)
    }

    @Override
    public void onFortifyEvent(Country countryFrom, Country countryTo, int numTroops) {
        // Put all the code to pick the right action here
        super.onFortifyEvent(countryFrom, countryTo, numTroops);
        // Add code for deciding end of event phase here (finish attack phase method)
    }

}
