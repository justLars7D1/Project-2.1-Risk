package bot.Algorithms.MCTS;

/**
 * Represents an action that can be taken in a current state to end up in another state
 */
public interface Action {
    /**
     * Performs the action
     *
     * @return The new state after performing the action
     */
    StateNode perform(StateNode state);
}
