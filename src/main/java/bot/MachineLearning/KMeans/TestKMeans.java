package bot.MachineLearning.KMeans;

import bot.Mathematics.LinearAlgebra.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TestKMeans {
    public static void main(String[] args) {

        KMeans alg = new KMeans();
        alg.compile(new RandomDataPointInit());

        Vector[] xs = new Vector[10];
        for (int i = 0; i < xs.length; i++) {
            if (i <= xs.length/2)
                xs[i] = new Vector(i, 0);
            else
                xs[i] = new Vector(i, 10);
        }

        System.out.println("Data: " + Arrays.toString(xs));

        HashMap<Vector, List<Vector>> bestResults = alg.train(xs, 2, 10);
        double bestScore = alg.evaluateCost(bestResults);

        int numTraining = 1000 - 1;
        for (int i = 0; i < numTraining; i++) {
            HashMap<Vector, List<Vector>> results = alg.train(xs, 2, 1000);
            double score = alg.evaluateCost(results);
            if (score > bestScore) {
                bestResults = results;
                bestScore = score;
            }
        }

        System.out.println("Results: " + bestResults);
        System.out.println("Cost: " + bestScore);

    }
}
