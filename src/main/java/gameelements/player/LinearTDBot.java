package gameelements.player;

import bot.Algorithms.MarkovChain.BattlePhaseEstimator;
import bot.MachineLearning.NeuralNetwork.Activations.Pass;
import bot.MachineLearning.NeuralNetwork.Losses.TDLoss;
import bot.MachineLearning.NeuralNetwork.Model;
import bot.MachineLearning.NeuralNetwork.Optimizers.TDOptimizer;
import bot.Mathematics.LinearAlgebra.Vector;
import environment.BorderSupplyFeatures;
import environment.WolfFeatures;
import gameelements.board.Country;
import gameelements.game.Game;
import environment.FeatureSpace;
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
        this.linearEvalFunction.compile(new TDLoss(), new TDOptimizer(0.05, 0.5));
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
        double winChanceThreshold = 0.5;
        double randomChanceThreshold = 0.2;

        //super.onAttackEvent(countryFrom, countryTo);
        // Add code for deciding end of event phase here (finish attack phase method)

        //return vector has the 0th index containing the country from, the 1st index containing the country to and the 2nd index containing the state value
        ArrayList<Vector> chosenCountriesStateValue = this.futureStateValue(currentGame);

        //extracting the selected country IDs from the vector of countries and statevalue
        int countryAttackFromID = (int) chosenCountriesStateValue.get(0).get(0) ; // intial get(0) returns the oth index of the arraylist, second get(0) for the vector element retrieval
        int countryAttackToID = (int) chosenCountriesStateValue.get(1).get(0) ;

        //getting the country class references from the IDs
        Country countryFromID = currentGame.getGameBoard().getCountryFromID(countryAttackFromID);
        Country countryToID = currentGame.getGameBoard().getCountryFromID(countryAttackToID);

        //calculating the win chance for the countries specified
        double winChance = BattlePhaseEstimator.winChance(countryToID.getNumSoldiers(), countryFromID.getNumSoldiers()-1);

        //If there is an 'obvious' attack where the win chance is greater than the threshold we attack straight away.
        if(winChance > winChanceThreshold){
            super.onAttackEvent(countryFromID, countryToID);
        } else { // this is the condition that the winChance is below the threshold to attack immediately.
            if(Math.random() < randomChanceThreshold ){ //there is a 20% chance that the countries will attack even if it's below the threshold to allow exploration - 'underdog'
                super.onAttackEvent(countryFromID, countryToID);
                Vector current = new Vector(armies_feature, territory_feature, (double)enemey_reinforce_feature, best_enemy_feature,hinterland_feature);
                Vector future  = chosenCountriesStateValue.get(2);
                linearEvalFunction.tdTrain(future, current);
            } else {// if there is no 'obvious attack', and the potential 'underdog' attack is also no considered then we will move onto the next battle phase.
                currentGame.nextBattlePhase();
            }
        }
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

    public ArrayList<Vector> futureStateValue(Game game){
        int final_attackingCountry = -1;
        int final_defendingCountry= -1;
        Vector stateValueVector = new Vector(1);
        Vector counryFromVector = new Vector(1);
        Vector countryToVector = new Vector(1);

        Player current_player = game.getCurrentPlayer();
        double maxExpectedStateValue = -1000;
        HashMap<Country,List<Country>> attackTargets = getAttackTargets(game);
        Set<Country> owned_countries = attackTargets.keySet();
        // The resulting Arraylist containing a vector for each contry from, .. to , state value
        List<Vector> fightingCountriesStateValue = new ArrayList<>();

        for(Country attackingCountry : owned_countries){
            int attackingTroops = attackingCountry.getNumSoldiers();

            for(Country defendingCountry: attackTargets.get(attackingCountry)){
                int defendingTroops = defendingCountry.getNumSoldiers();
                // Owner needed to revert state adjustment later
                Player defendingOwner = defendingCountry.getOwner();

                // Attacking troops = troops stationed in country -1 -> one has to stay behind
                double win_chance = BattlePhaseEstimator.winChance(defendingTroops, attackingTroops-1);
                // How many troops is the enemy expected to loose if he wins, how many troops is the current player expected to loose when he wins
                //int TroopLossCurrentP = pseudoLoss(attackingTroops, defendingTroops);
                double expected_loss_win = BattlePhaseEstimator.expectedLossForWin(defendingTroops, attackingTroops);
                int TroopLossCurrentP = (int) Math.round(expected_loss_win);
                // Safeguard should not happen
                if(TroopLossCurrentP > attackingTroops){TroopLossCurrentP = attackingTroops;}
                double expected_loss_loose = BattlePhaseEstimator.expectedDamageWhenLost(defendingTroops, attackingTroops);
                int TroopLossEnemy = (int) Math.round(expected_loss_loose);
                if(TroopLossEnemy > defendingTroops){TroopLossEnemy = defendingTroops;}

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
                    // Country from
                    final_attackingCountry = attackingCountry.getID();
                    // country to
                    final_defendingCountry = defendingCountry.getID();
                    // state value
                    maxExpectedStateValue = expectedStateValue;
                }
            }
        }
        stateValueVector.set(0,maxExpectedStateValue);
        counryFromVector.set(0,(double)final_attackingCountry);
        countryToVector.set(0,(double)final_defendingCountry);
        return new ArrayList<>(Arrays.asList(counryFromVector, countryToVector,stateValueVector ));
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
}
