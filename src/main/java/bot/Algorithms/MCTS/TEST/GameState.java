package bot.Algorithms.MCTS.TEST;

import bot.Algorithms.MCTS.Action;
import bot.Algorithms.MCTS.StateNode;

public class GameState extends StateNode {

    private double value;
    private static int counter = 0;

    public GameState(Action[] actions, double value) {
        super(actions);
        this.value = value;
    }

    @Override
    public boolean isTerminal() {
        boolean check = (counter++ >= 20);
        if (check) counter = 0;
        return check;
    }

    @Override
    public StateNode simulate(Action a) {
        return a.perform(this);
    }

    @Override
    public double calculateStateValue() {
        return value;
    }

    @Override
    public String toString() {
        return "value: " + value;
    }
}
