package bot.MachineLearning.NeuralNetwork;

import bot.Mathematics.LinearAlgebra.Vector;

public class Transition {

    private final Vector currentState;
    private final boolean attackAction;
    private final double reward;
    private final Vector nextState;

    public Transition(Vector currentState, boolean attackAction, double reward, Vector nextState) {
        this.currentState = currentState;
        this.attackAction = attackAction;
        this.reward = reward;
        this.nextState = nextState;
    }

    public Vector getCurrentState() {
        return currentState;
    }

    public boolean isAttackAction() {
        return attackAction;
    }

    public double getReward() {
        return reward;
    }

    public Vector getNextState() {
        return nextState;
    }

    @Override
    public String toString() {
        return "Transition{" +
                "currentState=" + currentState +
                ", attackAction=" + attackAction +
                ", reward=" + reward +
                ", nextState=" + nextState +
                '}';
    }
}
