package gameelements.player;

import bot.Algorithms.MarkovChain.BattlePhaseEstimator;
import bot.MachineLearning.NeuralNetwork.Activations.LeakyReLu;
import bot.MachineLearning.NeuralNetwork.Activations.Pass;
import bot.MachineLearning.NeuralNetwork.Activations.ReLu;
import bot.MachineLearning.NeuralNetwork.Activations.Sigmoid;
import bot.MachineLearning.NeuralNetwork.Losses.Loss;
import bot.MachineLearning.NeuralNetwork.Losses.MSE;
import bot.MachineLearning.NeuralNetwork.Model;
import bot.MachineLearning.NeuralNetwork.Optimizers.Optimizer;
import bot.MachineLearning.NeuralNetwork.Optimizers.RMSProp;
import bot.MachineLearning.NeuralNetwork.Optimizers.SGD;
import bot.Mathematics.LinearAlgebra.Vector;
import environment.BorderSupplyFeatures;
import environment.WolfFeatures;
import gameelements.board.Country;
import gameelements.game.Game;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DQNNBot extends RiskBot {
    /*
    * Double Q-Learning (Hasselt, 2010)
    *
    * Y = R[t+1] + gamma * Q'(S[t+1], argmax(a) Q(S[t+1], a)) 
    *
    * The weights of the online network are transfered to the
    * target network every n steps.
    *
    * sequential attack decision
    * state = (from, to, threat, ...) -> decision = (from, to, attack)
    *
    * predicts q values for a binary attack decision
    */

    private final static boolean trainingEnabled = false;

    private final static double discountFactor = 0.5;

    public final static boolean loadBestModel = true;

    Loss lossFunction = new MSE();
    Optimizer optEst = new SGD(0.05);
    Optimizer optTarget = new SGD(0.05);

    int n;

    Model targetNetwork;

    Model estimatorNetwork;

    /**
     * algorithm and strategies for our risk bot
     */
    public DQNNBot(int id, int numTroopsInInventory, Game game) {
        this(id, numTroopsInInventory, game, 8, 1);
    }

    public DQNNBot(int id, int numTroopsInInventory, Game game, int numFeatures, int lag) {
        super(id, numTroopsInInventory, game);

        this.n = lag;

        if (loadBestModel) {

            estimatorNetwork = Model.loadModel("src/main/java/gameelements/player/botWeights/bestEstimatorWeights1.txt");

        } else {

            // dynamic network
            estimatorNetwork = new Model(numFeatures);
            estimatorNetwork.addLayer(6, new ReLu());
            estimatorNetwork.addLayer(3, new ReLu());
            estimatorNetwork.addLayer(2, new Pass());

            estimatorNetwork.compile(lossFunction, optTarget);

        }

        // target approximation
        targetNetwork = new Model(numFeatures);
        targetNetwork.addLayer(6, new ReLu());
        targetNetwork.addLayer(3, new ReLu());
        targetNetwork.addLayer(2, new Pass());

        targetNetwork.compile(lossFunction, optEst);

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
        // Here we check if the phase is over, and if so, we compute which countries we could attack from
        computeCountriesToAttackFrom();
        turnReward = 0;
    }

    private List<List<Country>> countryFromToAttackPairs;

    public double turnReward = 0;
    private double totalReward = 0;

    private static int bla = 0;

    @Override
    public void onAttackEvent(Country countryFrom, Country countryTo) {

        boolean actionTaken = false;

        while (!actionTaken) {

            if(!countryFromToAttackPairs.isEmpty()){
                // Select the current pair we could potentially attack from and to
                List<Country> attackPair = countryFromToAttackPairs.get(0);
                countryFrom = attackPair.get(0);
                countryTo = attackPair.get(1);

                int numCountriesBeforeAttack = getNumCountriesOwned();
                int numTroopsDefenderBeforeAttack = countryFrom.getNumSoldiers();

                // Run it through the DQNN and evaluate
                Vector features = getPlayerFeatures(countryFrom, countryTo);
                Vector qValues = estimatorNetwork.forwardEvaluate(features);

                // Decide on taking the action or not
                boolean takeAction = qValues.get(1) > qValues.get(0);

                //System.out.println("--- Turn Start ---");
                //System.out.println(takeAction + ": " + countryFrom.getNumSoldiers() + " -> " + countryTo.getNumSoldiers());

                if (takeAction) {
                    super.onAttackEvent(countryFrom, countryTo);
                    actionTaken = true;
                }

                /*System.out.println("Attacked from " + countryFrom.getName() + " to " + countryTo.getName());
                System.out.println("Stats from troops: " + countryFrom.getNumSoldiers());
                System.out.println("Stats to troops: " + countryTo.getNumSoldiers());*/

                if (trainingEnabled) {
                    optEst.init(estimatorNetwork);

                    double reward = 10*(getNumCountriesOwned() - numCountriesBeforeAttack);
                    if (reward == 0) reward += 2*(countryFrom.getNumSoldiers() - numTroopsDefenderBeforeAttack);

                    totalReward += reward;

                    if (bla % 2 == 0) {
                        System.out.println(totalReward);
                    }

                    Vector newFeatures = getPlayerFeatures(countryFrom, countryTo);
                    Vector qValuesNextState = estimatorNetwork.evaluate(newFeatures);
                    double targetValue = Math.max(qValuesNextState.get(0), qValuesNextState.get(1));
                    targetValue *= discountFactor;
                    targetValue += reward;
                    Vector Yt = new Vector(2);
                    if (takeAction) {
                        Yt.set(0, qValues.get(0));
                        Yt.set(1, targetValue);
                    } else {
                        Yt.set(0, targetValue);
                        Yt.set(1, qValues.get(1));
                    }

                    //System.out.print(lossFunction.evaluate(qValues, Yt));

                    //System.out.println("Reward: " + reward);

                    estimatorNetwork.computeGradientsRL(newFeatures, qValues, Yt);
                    optEst.updateWeights(estimatorNetwork);
                    estimatorNetwork.resetGradients();

                    //System.out.println("--- Turn End ---");


                }

                // Code for deciding end of event phase here (finish attack phase method)
                countryFromToAttackPairs.remove(attackPair);
                updatePairList();
            }

            if (countryFromToAttackPairs.size() == 0) {
                currentGame.nextBattlePhase();
                actionTaken = true;
            }

        }

    }

    @Override
    public void onFortifyEvent(Country countryFrom, Country countryTo, int numTroops) {
        bla++;
        // Put all the code to pick the right action here
        super.onFortifyEvent(countryFrom, countryTo, numTroops);
        // Add code for deciding end of event phase here (finish attack phase method)
    }

    private Vector getCountryFeatures(Country countryFrom, Country countryTo){
        // enemy troops susceptible due to threat on their country
        double susceptible = WolfFeatures.averageThreatOn(countryTo);
        // troops suitible to send into battle based on the threat they face
        double suitible = WolfFeatures.averageThreatOn(countryFrom);

        double winChance = BattlePhaseEstimator.winChance(countryTo.getNumSoldiers(), countryFrom.getNumSoldiers()-1);

        // expected number of troops lost in battle
        double expectedLoss = BattlePhaseEstimator.expectedLoss(countryTo.getNumSoldiers(), countryFrom.getNumSoldiers()-1);
        // expected number of oponent troops defeated in a battle
        double expectedDamage = BattlePhaseEstimator.expectedDamage(countryTo.getNumSoldiers(), countryFrom.getNumSoldiers()-1);

        return new Vector(winChance, suitible, susceptible, expectedLoss, expectedDamage);
    }

    private Vector getPlayerFeatures(Country countryFrom, Country countryTo){
        // troops suitible to send into battle based on the threat they face
        double suitible = WolfFeatures.averageThreatOn(countryFrom);
        // enemy troops susceptible due to threat on their country
        double susceptible = WolfFeatures.averageThreatOn(countryTo);

        double ownArmies = BorderSupplyFeatures.getArmiesFeature(currentGame.getGameBoard(), this);
        double enemyArmies = BorderSupplyFeatures.getArmiesFeature(currentGame.getGameBoard(), countryTo.getOwner());

        double bestEnemy = BorderSupplyFeatures.getBestEnemyFeature(currentGame);
        double enemyReinforcement = BorderSupplyFeatures.getTotalEnemyReinforcement(currentGame);

        double ownTerritories = BorderSupplyFeatures.getTerritoriesFeature(this);
        double enemyTerritories = BorderSupplyFeatures.getTerritoriesFeature(countryTo.getOwner());

        return new Vector(suitible, susceptible, ownArmies, ownTerritories, enemyArmies, enemyTerritories, enemyReinforcement, bestEnemy);
    }

    private Vector getFeatures(Country countryFrom, Country countryTo){
        // troops suitible to send into battle based on the threat they face
        double suitible = WolfFeatures.averageThreatOn(countryFrom);
        // enemy troops susceptible due to threat on their country
        double susceptible = WolfFeatures.averageThreatOn(countryTo);

        double ownArmies = BorderSupplyFeatures.getArmiesFeature(currentGame.getGameBoard(), this);
        double enemyArmies = BorderSupplyFeatures.getArmiesFeature(currentGame.getGameBoard(), countryTo.getOwner());

        double bestEnemy = BorderSupplyFeatures.getBestEnemyFeature(currentGame);
        double enemyReinforcement = BorderSupplyFeatures.getTotalEnemyReinforcement(currentGame);

        double ownTerritories = BorderSupplyFeatures.getTerritoriesFeature(this);
        double enemyTerritories = BorderSupplyFeatures.getTerritoriesFeature(countryTo.getOwner());

        double ownHinterland = WolfFeatures.hinterland(this);
        double enemyHinterland = WolfFeatures.hinterland(countryTo.getOwner());

        double averageBSR = BorderSupplyFeatures.getAverageBSR(currentGame);

        return new Vector(suitible, susceptible, averageBSR, ownArmies, ownTerritories, ownHinterland, enemyArmies, enemyTerritories, enemyHinterland, enemyReinforcement, bestEnemy);
    }

    private void updatePairList() {
        List<List<Country>> toRemovePairs = new LinkedList<>();
        for (List<Country> attackPair: countryFromToAttackPairs) {
            Country from = attackPair.get(0);
            if (from.getNumSoldiers() == 1 || !countriesOwned.contains(from))
                toRemovePairs.add(attackPair);
        }
        countryFromToAttackPairs.removeAll(toRemovePairs);
    }

    private void computeCountriesToAttackFrom() {
        countryFromToAttackPairs = new LinkedList<>();
        for (Country c: countriesOwned) {
            if (c.getNumSoldiers() > 1) {
                for (Country n: c.getNeighboringCountries()) {
                    if (!countriesOwned.contains(n)) {
                        countryFromToAttackPairs.add(Arrays.asList(c, n));
                    }
                }
            }
        }
    }

    public Model getEstimatorNetwork() {
        return estimatorNetwork;
    }
}
