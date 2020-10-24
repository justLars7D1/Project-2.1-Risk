package bot.MachineLearning.KMeans;

import bot.Mathematics.LinearAlgebra.Vector;

public interface Initialization {
    public Vector[] createCentroids(Vector[] xs, int numClusters);
}
