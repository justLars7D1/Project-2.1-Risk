package bot.MachineLearning.NeuralNetwork;

import bot.Mathematics.LinearAlgebra.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExperienceReplayer {

    private final List<Transition> transitions;

    private final Random random;

    public ExperienceReplayer() {
        this.transitions = new ArrayList<>();
        this.random = new Random();
    }

    public ExperienceReplayer(int seed) {
        this.transitions = new ArrayList<>();
        this.random = new Random(seed);

    }

    public void addSample(Vector currentState, boolean attack, double reward, Vector nextState) {
        this.transitions.add(new Transition(currentState, attack, reward, nextState));
    }

    public Transition[] getSamples(int batchSize) {
        int minSize = Math.min(batchSize, transitions.size());
        Transition[] transitions = new Transition[minSize];
        for (int i = 0; i < minSize; i++) {
            transitions[i] = getSample();
        }
        return transitions;
    }

    private Transition getSample() {
        int index = random.nextInt(transitions.size());
        return transitions.get(index);
    }

    @Override
    public String toString() {
        return "ExperienceReplayer{" +
                "transitions=" + transitions +
                '}';
    }
}
