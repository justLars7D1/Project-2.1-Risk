package gameelements.player;

import bot.MachineLearning.NeuralNetwork.Activations.Pass;
import bot.MachineLearning.NeuralNetwork.Model;
import environment.BorderSupplyFeatures;
import environment.FeatureSpace;
import environment.WolfFeatures;
import gameelements.board.Country;
import gameelements.game.Game;

public class LinearTDBot extends RiskBot {


    Model linearEvalFunction;

    /**
     * algorithm and strategies for our risk bot
     */
    public LinearTDBot(int id, int numTroopsInInventory, Game game) {
        super(id, numTroopsInInventory, game);
        setupModel();
    }

    private void setupModel() {
        this.linearEvalFunction = new Model(8);
        this.linearEvalFunction.addLayer(1, new Pass());
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
        double armies_feature = BorderSupplyFeatures.getArmiesFeature(currentGame.getGameBoard(), currentGame.getCurrentPlayer());
        double territory_feature = BorderSupplyFeatures.getTerritoriesFeature(currentGame.getCurrentPlayer());
        int enemey_reinforce_feature = BorderSupplyFeatures.getTotalEnemyReinforcement(currentGame);
        double best_enemy_feature = BorderSupplyFeatures.getBestEnemyFeature(currentGame);
        double hinterland_feature = WolfFeatures.hinterland(currentGame.getCurrentPlayer());

        double features[] = {armies_feature, territory_feature, (double)enemey_reinforce_feature, best_enemy_feature,hinterland_feature};

        FeatureSpace.features.put(FeatureSpace.round, features);
        FeatureSpace.round ++;
        // Put all the code to pick the right action here
        //super.onAttackEvent(countryFrom, countryTo);
        // Add code for deciding end of event phase here (finish attack phase method)
    }

    @Override
    public void onFortifyEvent(Country countryFrom, Country countryTo, int numTroops) {
        // Put all the code to pick the right action here
        //super.onFortifyEvent(countryFrom, countryTo, numTroops);
        // Add code for deciding end of event phase here (finish attack phase method)
    }

}
