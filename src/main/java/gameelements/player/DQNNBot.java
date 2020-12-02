package gameelements.player;

import bot.MachineLearning.NeuralNetwork.Optimizers.RMSProp;
import bot.Mathematics.LinearAlgebra.Vector;

import bot.MachineLearning.NeuralNetwork.Optimizers.Adam;
import environment.WolfFeatures;
import gameelements.board.Country;

import gameelements.game.Game;

import bot.MachineLearning.NeuralNetwork.Model;
import bot.MachineLearning.NeuralNetwork.Activations.*;

import java.util.ArrayList;
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

    private final static double discountFactor = 0.8;

    int n;

    Model targetNetwork;

    Model estimatorNetwork;

    /**
     * algorithm and strategies for our risk bot
     */
    public DQNNBot(int id, int numTroopsInInventory, Game game) {
        this(id, numTroopsInInventory, game, 12, 1);
    }

    public DQNNBot(int id, int numTroopsInInventory, Game game, int numFeatures, int lag) {
        super(id, numTroopsInInventory, game);

        this.n = lag;

        // target approximation
        targetNetwork = new Model(numFeatures);
        targetNetwork.addLayer(3, new LeakyReLu());
        targetNetwork.addLayer(3, new LeakyReLu());
        targetNetwork.addLayer(1, new Pass());

        // dynamic network
        estimatorNetwork = new Model(numFeatures);
        estimatorNetwork.addLayer(3, new LeakyReLu());
        estimatorNetwork.addLayer(3, new LeakyReLu());
        estimatorNetwork.addLayer(1, new Pass());

        //TODO: Add loss functions
        targetNetwork.compile(null, new RMSProp(0.001, 0.9));
        estimatorNetwork.compile(null, new RMSProp(0.001, 0.9));
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

    private Vector getAttackFeatures(Country countryFrom, Country countryTo){
        // enemy troops susceptible due to threat on their country
        double susceptible = WolfFeatures.averageThreatOn(countryTo);
        // troops suitible to send into battle based on the threat they face
        double suitible = WolfFeatures.averageThreatOn(countryFrom);
        
        return new Vector(suitible, susceptible);
    }

    @Override
    public void onAttackEvent(Country countryFrom, Country countryTo) {

        // Select the current pair we could potentially attack from and to
        List<Country> attackPair = countryFromToAttackPairs.get(0);
        countryFrom = attackPair.get(0);
        countryTo = attackPair.get(1);

        // Run it through the DQNN and evaluate

        int numCountriesBeforeAttack = getNumCountriesOwned();

        // Decide on taking the action or not
        super.onAttackEvent(countryFrom, countryTo);

        System.out.println("Attacked from " + countryFrom.getName() + " to " + countryTo.getName());
        System.out.println("Stats from troops: " + countryFrom.getNumSoldiers());
        System.out.println("Stats to troops: " + countryTo.getNumSoldiers());

        if (trainingEnabled) {
            int reward = getNumCountriesOwned() - numCountriesBeforeAttack;

            // Train the bot
        }



        // Code for deciding end of event phase here (finish attack phase method)
        countryFromToAttackPairs.remove(attackPair);
        updatePairList();

        if (countryFromToAttackPairs.size() == 0) {
            System.out.println("Test");
            currentGame.nextBattlePhase();
        }

    }

    @Override
    public void onFortifyEvent(Country countryFrom, Country countryTo, int numTroops) {
        // Put all the code to pick the right action here
        // super.onFortifyEvent(countryFrom, countryTo, numTroops);
        // Add code for deciding end of event phase here (finish attack phase method)
        return;
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

}
