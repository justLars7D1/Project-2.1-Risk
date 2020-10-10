package bot.Algorithms.GeneticAlgorithm;

import java.util.Random;

/**
 * This class represents an agent within a certain environment
 * @author Lars Quaedvlieg <lcpm.quaedvlieg@student.maastrichtuniversity.nl> <Larsquaedvlieg@outlook.com>
 * @version 1.0
 */
public class Agent {
    /**
     * The ID of the agent (since pools of them can be created)
     */
    private final int ID;
    /**
     * The array containing the numerical weights of the agents
     */
    private double[] weights;
    /**
     * The score that the agent received acting in it's environment
     */
    private double score;

    /**
     * The ID counter for all agents
     */
    private static int IDCounter = 0;

    /**
     * One of the class constructors
     * @param num_weights The number of weights the agent should have
     * @param min_rand_num The minimum value a weight can have
     * @param max_rand_num The maximum value a weight can have
     */
    public Agent(int num_weights, double min_rand_num, double max_rand_num) {
        this.ID = IDCounter++;
        this.score = 0;
        this.weights = new double[num_weights];
        Random r = new Random();
        for (int i = 0; i<num_weights; i++) {
            weights[i] = min_rand_num + (max_rand_num - min_rand_num) * r.nextDouble();
        }
    }

    /**
     * Getter: ID
     * @return the ID of the agent
     */
    public int getID() {
        return ID;
    }
    /**
     * Getter: Weights
     * @return the weights of the agent
     */
    public double[] getWeights() {
        return weights;
    }

    /**
     * Setter: Weights
     * @param weights The weights that you want to give to the agent
     */
    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    /**
     * Getter: Score
     * @return the score of the agent within it's environment
     */
    public double getScore() {
        return score;
    }

    /**
     * Setter: Score
     * @param score The score of the agent within it's environment
     */
    public void setScore(double score) {
        this.score = score;
    }

    //Testing the agent class
    public static void main(String[] args) {
        final int NUM_AGENTS = 100;
        final int NUM_WEIGHTS = 4;
        final double MAX_RAND_NUM = 3;
        final double MIN_RAND_NUM = -3;

        Agent[] agents = new Agent[NUM_AGENTS];
        for (int i = 0; i < NUM_AGENTS; i++) agents[i] = new Agent(NUM_WEIGHTS, MIN_RAND_NUM, MAX_RAND_NUM);
        for (Agent agent : agents) {
            for (double weight: agent.getWeights()) {
                System.out.print(weight + " ");
            }
            System.out.println();
        }
    }
}
