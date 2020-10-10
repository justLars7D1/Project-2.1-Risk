package bot.Algorithms.MCTS.TEST;

import bot.Algorithms.MCTS.Action;
import bot.Algorithms.MCTS.StateNode;

public class TestAction implements Action {

    private char type;

    public TestAction(char type) {
        assert (type == 'l' || type == 'r');
        this.type = type;
    }

    @Override
    public StateNode perform(StateNode state) {
        Action[] a = {new TestAction('l'), new TestAction('r')};
        double val = 0;
        if (type == 'r') {
            val = 10;
        } else if (type == 'l') {
            val = 15;
        }
        return new GameState(a, val);
    }

    @Override
    public String toString() {
        return "Action: " + type;
    }
}
