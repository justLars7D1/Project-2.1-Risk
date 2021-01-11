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

    public void addSample(Vector currentState, boolean attack, int reward, Vector nextState) {
        this.transitions.add(new Transition(currentState, attack, reward, nextState));
    }

    public Transition[] getSamples(int batchSize) {
        Transition[] transitions = new Transition[batchSize];
        for (int i = 0; i < batchSize; i++) {
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
