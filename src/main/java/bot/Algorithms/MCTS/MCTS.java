package bot.Algorithms.MCTS;

import bot.Algorithms.MCTS.SearchFunctions.SearchFunction;
import bot.Algorithms.MCTS.StoppingConditions.StoppingCondition;

import java.util.List;
import java.util.Random;

/**
 * MCTS is a class that contains all logic and some implemented part of the Monte Carlo Tree Search method
 */
public class MCTS {

    /**
     * The function we will use to evaluate which state to explore next,
     * Making a trade-off between exploration and exploitation
     */
    private final SearchFunction explorationFunction;

    /**
     * The stopping condition for the entire algorithm.
     * (Usually we use time for this, but this can be defined by the implementor themselves this way)
     */
    private final StoppingCondition stoppingCondition;

    /**
     * Constructor
     *
     * @param searchFunction    The search evaluation function
     * @param stoppingCondition The stopping condition
     */
    public MCTS(SearchFunction searchFunction, StoppingCondition stoppingCondition) {
        this.explorationFunction = searchFunction;
        this.stoppingCondition = stoppingCondition;
    }

    /**
     * Find the best action to perform next. This is the heart of the class
     *
     * @param stateNode The current state to explore
     * @return The best action to take in this current state
     */
    public Action findBestAction(StateNode stateNode) {
        stoppingCondition.init();

        while (!stoppingCondition.isReached()) {
            updateTree(stateNode);
        }

        return selectBestAction(stateNode);
    }


    /**
     * Updates the tree values in the tree. This corresponds to one iteration of the algorithm.
     *
     * @param stateNode The state to explore
     */
    private void updateTree(StateNode stateNode) {
        StateNode currentNode = stateNode;
        while (!currentNode.isLeafNode()) {
            currentNode = explorationFunction.findBestStateToExplore(currentNode);
        }
        if (currentNode.getSampleCount() != 0) {
            // Get first new child node after performing the actions
            if (!currentNode.isTerminal()) currentNode = currentNode.performActions();
        }
        double rolloutValue = rollout(currentNode);
        backPropagate(currentNode, rolloutValue);
    }

    /**
     * Perform the rollout phase of the algorithm. Basically the Monte Carlo Simulation part
     *
     * @param currentState The state to perform the rollout on
     * @return The value that comes from the simulation
     */
    private double rollout(StateNode currentState) {
        // Loop until stopping condition
        while (true) {
            if (currentState.isTerminal()) {
                return currentState.calculateStateValue();
            }
            Action randomAction = chooseRandomAction(currentState);
            currentState = currentState.simulate(randomAction);
        }
    }

    /**
     * Propagate the score back to the top of the tree, updating the states and their parents
     *
     * @param currentState    The current state that rollout was performed on
     * @param simulationValue The value given by the simulation/rollout
     */
    private void backPropagate(StateNode currentState, double simulationValue) {
        currentState.addSampled();
        currentState.addToTotalScore(simulationValue);
        currentState = currentState.getParent();
        while (currentState != null) {
            currentState.addSampled();
            currentState.addToTotalScore(simulationValue);
            currentState = currentState.getParent();
        }
    }

    /**
     * Selects the best action to perform in a state based on the scoring results of the tree search
     *
     * @param s The state to select the best action of
     * @return The best action
     */
    private Action selectBestAction(StateNode s) {
        List<StateNode> children = s.getChildNodes();
        Action[] actions = s.getActions();
        double highestAvgValue = 0;
        Action bestAction = actions[0];

        for (int i = 0; i < actions.length; i++) {
            StateNode correspondingState = children.get(i);
            double avgValue = correspondingState.getTotalScore() / correspondingState.getSampleCount();
            if (avgValue > highestAvgValue) {
                highestAvgValue = avgValue;
                bestAction = actions[i];
            }
        }

        return bestAction;
    }

    /**
     * Chooses a random action using a uniform distribution (all numbers have about equal probability)
     *
     * @param s The state
     * @return The randomly selected action
     */
    private Action chooseRandomAction(StateNode s) {
        Random rand = new Random();
        Action[] actions = s.getActions();
        int index = rand.nextInt(actions.length);
        return actions[index];
    }

}
