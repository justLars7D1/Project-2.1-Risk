package bot.Algorithms.MCTS.TEST;

import bot.Algorithms.MCTS.Action;
import bot.Algorithms.MCTS.MCTS;
import bot.Algorithms.MCTS.SearchFunctions.SearchFunction;
import bot.Algorithms.MCTS.SearchFunctions.UCB1;
import bot.Algorithms.MCTS.StoppingConditions.StoppingCondition;
import bot.Algorithms.MCTS.StoppingConditions.TimeCondition;

public class Main {
    public static void main(String[] args) {
        Action[] actions = {new TestAction('l'), new TestAction('r')};
        double value = 0;
        GameState initialState = new GameState(actions, value);

        SearchFunction f = new UCB1();
        StoppingCondition s = new TimeCondition(2000);
        MCTS searchAlgorithm = new MCTS(f, s);

        Action a = searchAlgorithm.findBestAction(initialState);

        System.out.println(a);

    }
}
