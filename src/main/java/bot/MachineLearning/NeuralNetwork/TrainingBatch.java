package bot.MachineLearning.NeuralNetwork;

import bot.Mathematics.LinearAlgebra.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrainingBatch {

    private final Vector[] xs;
    private final Vector[] ys;

    public TrainingBatch(Vector[] x, Vector[] y) {
        this.xs = x;
        this.ys = y;
    }

    public Vector[] getXs() {
        return xs;
    }

    public Vector[] getYs() {
        return ys;
    }

    public int getSize() {
        return xs.length;
    }

    public static TrainingBatch[] generateUniformlyRandomBatches(Vector[] xs, Vector[] ys, int batchSize) {
        assert xs.length == ys.length;
        int numBatches = (int) Math.ceil(((double) xs.length)/batchSize);
        TrainingBatch[] batches = new TrainingBatch[numBatches];

        List<Integer> availableSamples = new ArrayList<>();
        int setSize = xs.length;
        for (int i = 0; i < setSize; i++) availableSamples.add(i);

        Random r = new Random();
        for (int i = 0; i < numBatches; i++) {
            List<Vector> xBatch = new ArrayList<>();
            List<Vector> yBatch = new ArrayList<>();
            for (int j = 0; j < batchSize && setSize > 0; j++) {
                int randIndex = r.nextInt(setSize--);
                int sampleIndex = availableSamples.get(randIndex);
                xBatch.add(xs[sampleIndex]);
                yBatch.add(ys[sampleIndex]);
                availableSamples.remove(randIndex);
            }
            Vector[] batchX = xBatch.toArray(new Vector[0]);
            Vector[] batchY = yBatch.toArray(new Vector[0]);
            TrainingBatch batch = new TrainingBatch(batchX, batchY);
            batches[i] = batch;
        }

        return batches;
    }

}
