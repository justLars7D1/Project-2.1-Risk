package gameelements.player;

import bot.Algorithms.MarkovChain.BattlePhaseEstimator;
import bot.MachineLearning.NeuralNetwork.Activations.Pass;
import bot.MachineLearning.NeuralNetwork.Activations.ReLu;
import bot.MachineLearning.NeuralNetwork.Losses.Loss;
import bot.MachineLearning.NeuralNetwork.Losses.MSE;
import bot.MachineLearning.NeuralNetwork.Model;
import bot.MachineLearning.NeuralNetwork.Optimizers.Optimizer;
import bot.MachineLearning.NeuralNetwork.Optimizers.SGD;
import bot.MachineLearning.NeuralNetwork.GameMetricCollector;
import bot.MachineLearning.NeuralNetwork.TurnMetricCollector;
import bot.Mathematics.LinearAlgebra.Vector;
import environment.BorderSupplyFeatures;
import environment.WolfFeatures;
import gameelements.board.Country;
import gameelements.game.Game;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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

    private int gameNumber;

    private final static boolean trainingEnabled = true;

    private final static double discountFactor = 0.8;

    public final static boolean loadBestModel = false;

    Loss lossFunction = new MSE();
    Optimizer optEst = new SGD(10e-3);
    Optimizer optTarget = new SGD(10e-3);

    int lag;

    Model targetNetwork;

    Model estimatorNetwork;

    private final double randomActionProbability = 0.5;
    private Random random = new Random();

    public GameMetricCollector gameMetrics = new GameMetricCollector();

    {
        gameMetrics.enableMetric("gameId");
        gameMetrics.enableMetric("reward");
        gameMetrics.enableMetric("targetOffset");
        gameMetrics.enableMetric("estimatorOffset");
        gameMetrics.enableMetric("estimatorAttack");
        gameMetrics.enableMetric("exploration");
        gameMetrics.enableMetric("lossInTroops");
        gameMetrics.enableMetric("totalNumTroops");
    }

    public TurnMetricCollector turnMetrics = new TurnMetricCollector();
    {
        turnMetrics.enableMetric("gameId");
        turnMetrics.enableMetric("totalNumTroops");
    }

    /**
     * algorithm and strategies for our risk bot
     */
    public DQNNBot(int id, int numTroopsInInventory, Game game) {
        this(id, numTroopsInInventory, game, 2, 2);
    }

    public DQNNBot(int id, int numTroopsInInventory, Game game, int numFeatures, int lag) {
        super(id, numTroopsInInventory, game);

        this.lag = lag;

        if (loadBestModel) {

            estimatorNetwork = Model.loadModel("src/main/java/gameelements/player/botWeights/bestEstimatorWeights2.txt");
            targetNetwork = Model.loadModel("src/main/java/gameelements/player/botWeights/bestTargetWeights2.txt");

        } else {

            // dynamic network
            estimatorNetwork = new Model(numFeatures);
            estimatorNetwork.addLayer(6, new ReLu());
            estimatorNetwork.addLayer(3, new ReLu());
            estimatorNetwork.addLayer(2, new Pass());

            estimatorNetwork.compile(lossFunction, optEst);

        }

        // target approximation
        targetNetwork = new Model(numFeatures);
        targetNetwork.addLayer(6, new ReLu());
        targetNetwork.addLayer(3, new ReLu());
        targetNetwork.addLayer(2, new Pass());

        targetNetwork.compile(lossFunction, optTarget);

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
    }

    private List<List<Country>> countryFromToAttackPairs;
    private int episodeSize;

    private int iteration = 0;
    private int turnNum = 0;

    @Override
    public void onAttackEvent(Country countryFrom, Country countryTo) {

        boolean actionTaken = false;

        while (!actionTaken) {

            if(!countryFromToAttackPairs.isEmpty()){

                gameMetrics.addToMetric("gameId", gameNumber);

                gameMetrics.addToMetric("totalNumTroops", getNumTroopsOwned());

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
                if (random.nextDouble() <= this.randomActionProbability) {
                    takeAction = !takeAction;
                    gameMetrics.addToMetric("exploration", 1);
                } else {
                    gameMetrics.addToMetric("exploration", 0);
                }

                gameMetrics.addToMetric("estimatorAttack", takeAction ? 1 : 0);

                //System.out.println("--- Turn Start ---");
                //System.out.println(takeAction + ": " + countryFrom.getNumSoldiers() + " -> " + countryTo.getNumSoldiers());

                if (takeAction) {
                    super.onAttackEvent(countryFrom, countryTo);
                    actionTaken = true;
                }

                /*System.out.println("Attacked from " + countryFrom.getName() + " to " + countryTo.getName());
                System.out.println("Stats from troops: " + countryFrom.getNumSoldiers());
                System.out.println("Stats to troops: " + countryTo.getNumSoldiers());*/

                optEst.init(estimatorNetwork);

                double reward = 10*(getNumCountriesOwned() - numCountriesBeforeAttack);
                if (reward == 0) reward += (countryFrom.getNumSoldiers() - numTroopsDefenderBeforeAttack);
                if (reward == 0) reward = -1;

                gameMetrics.addToMetric("lossInTroops", numTroopsDefenderBeforeAttack - countryFrom.getNumSoldiers());
                gameMetrics.addToMetric("reward", reward);

                Vector newFeatures = getPlayerFeatures(countryFrom, countryTo);

                Vector qValuesNextStateEst = estimatorNetwork.evaluate(newFeatures);
                int maxIndex = (qValuesNextStateEst.get(0) >= qValuesNextStateEst.get(1)) ? 0 : 1;

                double targetValue = targetNetwork.evaluate(newFeatures).get(maxIndex);
                //double targetValue = Math.max(qValuesNextStateEst.get(0), qValuesNextStateEst.get(1)); - Normal Q-Learning

                targetValue *= discountFactor;
                targetValue += reward;
                Vector Yt = new Vector(2);
                Yt.set(0, targetValue);
                Yt.set(1, targetValue);

                gameMetrics.addToMetric("estimatorOffset", lossFunction.evaluate(qValues, Yt).getMagnitude());
                gameMetrics.addToMetric("targetOffset", lossFunction.evaluate(targetNetwork.evaluate(features), Yt).getMagnitude());

                //System.out.print(lossFunction.evaluate(qValues, Yt));

                //System.out.println("Reward: " + reward);
                if (trainingEnabled) {

                    estimatorNetwork.computeGradientsRL(newFeatures, qValues, Yt);

                    if (iteration % this.lag == 0) {
                        estimatorNetwork.copyModelWeights(targetNetwork);
                    }

                }

                //System.out.println("--- Turn End ---");
                iteration++;

                // Code for deciding end of event phase here (finish attack phase method)
                countryFromToAttackPairs.remove(attackPair);
                updatePairList();
            }

            if (countryFromToAttackPairs.size() == 0) {

                if (trainingEnabled) {
                    estimatorNetwork.averageGradients(episodeSize);
                    optEst.updateWeights(estimatorNetwork);
                    estimatorNetwork.resetGradients();
                }

                turnMetrics.addToMetric("gameId", gameNumber);
                turnMetrics.addToMetric("totalNumTroops", getNumTroopsOwned());

                turnNum = 0;

                currentGame.nextBattlePhase();
                actionTaken = true;
            }

        }

    }

    @Override
    public void onFortifyEvent(Country countryFrom, Country countryTo, int numTroops) {
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
        // expected number of opponent troops defeated in a battle
        double expectedDamage = BattlePhaseEstimator.expectedDamage(countryTo.getNumSoldiers(), countryFrom.getNumSoldiers()-1);

        double ownArmies = BorderSupplyFeatures.getArmiesFeature(currentGame.getGameBoard(), this);

        double enemyArmies = BorderSupplyFeatures.getArmiesFeature(currentGame.getGameBoard(), countryTo.getOwner());

        double bestEnemy = BorderSupplyFeatures.getBestEnemyFeature(currentGame);

        return new Vector(winChance, suitible, susceptible, expectedLoss, expectedDamage, ownArmies, enemyArmies, bestEnemy);
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

        return new Vector(countryFrom.getNumSoldiers(), countryTo.getNumSoldiers());

        //return new Vector(suitible, susceptible, ownArmies, ownTerritories, enemyArmies, enemyTerritories, enemyReinforcement, bestEnemy);
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
        this.episodeSize = countryFromToAttackPairs.size();
    }

    @Override
    public void reset(int numTroopsInInventory) {
        gameNumber++;
        iteration = 0;
        turnNum = 0;
        super.reset(numTroopsInInventory);
    }

    public void saveNetworks(String dir) {
        targetNetwork.save(dir + "-target.txt");
        estimatorNetwork.save(dir + "-estimator.txt");
    }

    public void saveMetrics(String dir) {
        gameMetrics.saveToFile(dir + "-game-metrics.txt");
        turnMetrics.saveToFile(dir + "-turn-metrics.txt");

    }

    public Model getEstimatorNetwork() {
        return estimatorNetwork;
    }

    public Model getTargetNetwork() {
        return targetNetwork;
    }
}
