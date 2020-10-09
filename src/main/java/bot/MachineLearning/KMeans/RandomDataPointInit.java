package MachineLearning.KMeans;

import bot.Mathematics.LinearAlgebra.Vector;

import java.util.Random;

public class RandomDataPointInit implements Initialization {
    @Override
    public Vector[] createCentroids(Vector[] xs, int numClusters) {
        Random r = new Random();
        Vector[] centroids = new Vector[numClusters];
        for (int i = 0; i < numClusters; i++) {
            int index = r.nextInt(xs.length);
            centroids[i] = (Vector) xs[i].clone();
        }
        return centroids;
    }
}
