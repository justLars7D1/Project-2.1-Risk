package gameelements.player;

import bot.Algorithms.MarkovChain.BattlePhaseEstimator;
import bot.MachineLearning.NeuralNetwork.Activations.Pass;
import bot.MachineLearning.NeuralNetwork.Model;
import bot.Mathematics.LinearAlgebra.Vector;
import environment.BorderSupplyFeatures;
import environment.WolfFeatures;
import gameelements.board.Country;
import gameelements.game.Game;
import environment.FeatureSpace;
import org.junit.Test;

import java.util.*;

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
        this.linearEvalFunction = new Model(5);
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

    public Vector calculateFeatures(Game game){
        double armies_feature = BorderSupplyFeatures.getArmiesFeature(game.getGameBoard(), game.getCurrentPlayer());
        double territory_feature = BorderSupplyFeatures.getTerritoriesFeature(game.getCurrentPlayer());
        int enemey_reinforce_feature = BorderSupplyFeatures.getTotalEnemyReinforcement(game);
        double best_enemy_feature = BorderSupplyFeatures.getBestEnemyFeature(game);
        double hinterland_feature = WolfFeatures.hinterland(game.getCurrentPlayer());

        Vector input = new Vector(armies_feature, territory_feature, enemey_reinforce_feature, best_enemy_feature, hinterland_feature);
        return input;
    }

    public HashMap<List<Country>, Vector> futureStateValue(Game game){
        // Features for win state
        double armies_feature;
        double territory_feature;
        int enemey_reinforce_feature;
        double best_enemy_feature;
        double hinterland_feature;


        Player current_player = game.getCurrentPlayer();
        double maxExpectedStateValue = -1000;
        HashMap<Country,List<Country>> attackTargets = getAttackTargets(game);
        Set<Country> owned_countries = attackTargets.keySet();
        // The returned HashMap
        HashMap<List<Country>, Vector> fightingCountriesStateValue = new HashMap<>();

        List<Country> fightingCountries = new ArrayList<>(2);

        for(Country attackingCountry : owned_countries){
            int attackingTroops = attackingCountry.getNumSoldiers();

            for(Country defendingCountry: attackTargets.get(attackingCountry)){
                int defendingTroops = defendingCountry.getNumSoldiers();
                // Owner needed to revert state adjustment later
                Player defendingOwner = defendingCountry.getOwner();

                // Attacking troops = troops stationed in country -1 -> one has to stay behind
                double win_chance = BattlePhaseEstimator.winChance(defendingTroops, attackingTroops-1);
                // How many troops is the enemy expected to loose if he wins, how many troops is the current player expected to loose when he wins
                int TroopLossCurrentP = pseudoLoss(attackingTroops, defendingTroops);
                int TroopLossEnemy = pseudoLoss(defendingTroops, attackingTroops);

                /* Attacking Player wins */
                // 1. defending country troops = 0; 2. attacking country - expected loss ; 3. Switch Owner
                defendingCountry.removeNumSoldiers(defendingCountry.getNumSoldiers());
                attackingCountry.removeNumSoldiers(TroopLossCurrentP);
                current_player.checkCountryConquer(attackingCountry,defendingCountry);

                // Calculate State value and feature values of winning state -> add eval function here
                Vector win_value = this.linearEvalFunction.evaluate(calculateFeatures(game));

                // Revert Game State Changes
                // 1. reset defender troops -> 2. change owner defender country -> 3. set army strength defender -> 4. reset attacking country troops -> 4. set army strength attacking country
                defendingCountry.removeNumSoldiers(defendingCountry.getNumSoldiers());
                defendingOwner.checkCountryConquer(defendingCountry,defendingCountry);
                defendingCountry.addNumSoldiers(defendingTroops);
                attackingCountry.removeNumSoldiers(attackingCountry.getNumSoldiers());
                attackingCountry.addNumSoldiers(attackingTroops);

                /* Defending Player wins */
                // 1. Set attacking army to 1 -> 2. Reduce defending army by expected loss
                attackingCountry.removeNumSoldiers(attackingCountry.getNumSoldiers()-1);
                defendingCountry.removeNumSoldiers(TroopLossEnemy);

                // Calculate State value and feature value for loosing state -> add eval function here
                Vector loose_value = this.linearEvalFunction.evaluate(calculateFeatures(game));

                // Revert Game State Changes
                // 1. reset defending troops -> 2. set defending troop strength -> 3. set attacking troop strength
                defendingCountry.removeNumSoldiers(defendingCountry.getNumSoldiers());
                defendingCountry.addNumSoldiers(defendingTroops);
                attackingCountry.addNumSoldiers(attackingTroops-1);

                // Calculate expected State Value
                double expectedStateValue = win_chance * win_value.get(0) + (1-win_chance) * loose_value.get(0);

                // Find the maximum expected state value and the countries that belong to it
                if(expectedStateValue > maxExpectedStateValue){
                    maxExpectedStateValue = expectedStateValue;
                    fightingCountries.add(0,attackingCountry);
                    fightingCountries.add(1,defendingCountry);
                }
            }
        }
        fightingCountriesStateValue.put(fightingCountries, new Vector(maxExpectedStateValue));
        return fightingCountriesStateValue;
    }

    public static HashMap getAttackTargets(Game game){
        Player current_player = game.getCurrentPlayer();
        HashSet<Country> owned_countries = current_player.getCountriesOwned();
        HashMap<Country,List<Country>> attackTargets = new HashMap<>();
        List<Country> targetCountries;

        for(Country owned_country: owned_countries){
            targetCountries = new ArrayList<>();
            for (Country neighbor_country :owned_country.getNeighboringCountries()){
                if(neighbor_country.getOwner() != current_player){
                    targetCountries.add(neighbor_country);
                }
            }
            attackTargets.put(owned_country, targetCountries);
        }
        return attackTargets;
    }

    // We want to know how many troops the winner will loose as the loose will loose all
    public static int pseudoLoss(int winnerTroops, int looserTroops){return 2;}

}
