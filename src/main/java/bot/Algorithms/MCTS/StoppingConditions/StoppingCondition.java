package bot.Algorithms.MCTS.StoppingConditions;

/**
 * Represents an abstract notion of a stopping condition of the search algorithm
 */
public interface StoppingCondition {

    /**
     * Initialize the condition
     */
    void init();

    /**
     * Check whether the condition has been reached
     *
     * @return Whether the condition has been reached
     */
    boolean isReached();

}
