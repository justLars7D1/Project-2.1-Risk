package bot.Algorithms.GeneticAlgorithm;

import java.util.Arrays;

/**
 * This class represents the genetic algorithm strategy. It takes a pool of agents and with a score function it can run
 * @author Lars Quaedvlieg <lcpm.quaedvlieg@student.maastrichtuniversity.nl> <Larsquaedvlieg@outlook.com>
 * @version 1.0
 */
public class GeneticAlgorithm {

    /**
     * The number of generations that the algorithm can at most run
     */
    private final int NUM_GENERATIONS;
    /**
     * The pool of agents
     */
    private Agent[] agentPool;

    /**
     * The current generation
     */
    private int current_generation;

    /**
     * One of the class constructors
     * @param num_generations The maximum number of generations that it can reproduce
     * @param num_agents The number of agents it should use in the algorithm
     * @param num_weights The number of weights the agents should have
     * @param min_rand_num The minimum weight value
     * @param max_rand_num The maximum weight value
     */
    public GeneticAlgorithm(int num_generations, int num_agents, int num_weights, double min_rand_num, double max_rand_num) {
        this.NUM_GENERATIONS = num_generations;
        this.current_generation = 0;
        this.agentPool = new Agent[num_agents];
        for(int i = 0; i<num_agents; i++) agentPool[i] = new Agent(num_weights, min_rand_num, max_rand_num);
    }

    /**
     * One of the class constructors
     * @param num_agents The number of agents it should use in the algorithm
     * @param num_weights The number of weights the agents should have
     * @param min_rand_num The minimum weight value
     * @param max_rand_num The maximum weight value
     */
    public GeneticAlgorithm(int num_agents, int num_weights, double min_rand_num, double max_rand_num) {
        this.NUM_GENERATIONS = Integer.MAX_VALUE;
        this.current_generation = 0;
        this.agentPool = new Agent[num_agents];
        for(int i = 0; i<num_agents; i++) agentPool[i] = new Agent(num_weights, min_rand_num, max_rand_num);
    }

    /**
     * One of the class constructors
     * @param agentPool The predefined pool of agents
     */
    public GeneticAlgorithm(Agent[] agentPool) {
        this.NUM_GENERATIONS = Integer.MAX_VALUE;
        this.current_generation = 1;
        this.agentPool = agentPool;
    }

    /**
     * One of the class constructors
     * @param num_generations The maximum number of generations the algorithm can reproduce for
     * @param agentPool The predefined pool of agents
     */
    public GeneticAlgorithm(int num_generations, Agent[] agentPool) {
        this.NUM_GENERATIONS = num_generations;
        this.current_generation = 1;
        this.agentPool = agentPool;
    }

    /**
     * This method, if allowed, generates a new generation of agents based on the previous agents' scores
     * It picks two agents from the previous pool and splits each of their weights (genes) 50/50
     * Each new gene has a 5% chance to mutate, adding/subtracting up to 20% of it's current value
     */
    public void reproduce() {
        //Check if the number of generations has been reached. If so, return.
        if (this.current_generation++ >= this.NUM_GENERATIONS) return;
        //Create a new array for the new generation of agents
        Agent[] newAgentPool = new Agent[this.agentPool.length];
        int num_weights = this.agentPool[0].getWeights().length;

        //Sort by score decreasing (highest score first)
        Arrays.sort(this.agentPool, (a,b) -> String.valueOf(b.getScore()).compareTo(String.valueOf(a.getScore())));

        for (int i = 0; i<newAgentPool.length; i++) {
            //Pick two new agents with the proper probability distribution
            Agent[] chosenAgents = chooseAgentsForReproduction();

            //Now split their genes into a new agent (including mutation probability)
            Agent newAgent = new Agent(num_weights, 0, 0);
            double[] weightsHolder = new double[num_weights];

            int splitIndex = (int) (Math.random() * num_weights);
            int chosenFirstAgent = (Math.random() < 0.5) ? 0 : 1;
            for(int j = 0; j < num_weights; j++) {
                if (j <= splitIndex) {
                    weightsHolder[j] = chosenAgents[chosenFirstAgent].getWeights()[j];
                } else {
                    weightsHolder[j] = chosenAgents[1-chosenFirstAgent].getWeights()[j];
                }
                if (Math.random() <= 0.025) {
                    weightsHolder[j] += (Math.random()*0.2)*weightsHolder[j];
                } else if (Math.random() <= 0.05) {
                    weightsHolder[j] -= (Math.random()*0.2)*weightsHolder[j];
                }
            }
            //!!REMOVE!!// (For logical testing purposes only)
            newAgent.setScore(weightsHolder[0]);

            //Add the new agent to the new pool
            newAgent.setWeights(weightsHolder);
            newAgentPool[i] = newAgent;
        }

        this.agentPool = newAgentPool;

    }

    /**
     * This method chooses the two agents that need to be reproduced from the agent pool
     * @return the two selected agents in an array containing them as objects
     */
    private Agent[] chooseAgentsForReproduction() {
        //Pick a random number in the range of the sum of all scores
        double sumOfScores = 0;
        for (Agent a: this.agentPool) {
            sumOfScores += a.getScore();
        }
        double r = Math.random()*sumOfScores;

        //Pick two random agents for reproduction based on the sum of scores
        final int N_AGENTS = 2;
        Agent[] chosenAgents = new Agent[N_AGENTS];
        for (int i = 0; i < N_AGENTS; i++) {
            for(Agent a: this.agentPool) {
                r -= a.getScore();
                if (r <= 0) {
                    chosenAgents[i] = a;
                    r = Math.random()*sumOfScores;
                    break;
                }
            }
        }

        //Returns the two agents
        return chosenAgents;
    }

    /**
     * This prints the pool of agents with their ID, scores and weights
     */
    public void showAgentPool() {
        for(Agent a: this.agentPool) {
            System.out.println("id: " + a.getID() + ", score: " + a.getScore() + ", weights: "+ Arrays.toString(a.getWeights()));
        }
        System.out.println();
    }

    /**
     * Getter: current generation
     * @return The current generation of the agents
     */
    public int getCurrent_generation() {
        return current_generation;
    }

    public static void main(String[] args) {
        Agent[] agents = new Agent[100];
        for(int i = 0; i < agents.length; i++) {
            Agent a = new Agent(1, -1, 1);
            a.setWeights(new double[]{i});
            a.setScore(i);
            agents[i] = a;
        }

        GeneticAlgorithm test = new GeneticAlgorithm(agents);

        System.out.println("Generation 0");
        test.showAgentPool();

        for(int i = 1; i<1000; i++) {
            test.reproduce();
        }

        System.out.println("Final generation: "+test.getCurrent_generation());
        test.showAgentPool();

    }

}
