package MachineLearning.KMeans;

import bot.Mathematics.LinearAlgebra.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KMeans {

    private Initialization clusterInit;

    public void compile(Initialization clusterInit) {
        this.clusterInit = clusterInit;
    }

    public HashMap<Vector, List<Vector>> train(Vector[] xs, int numClusters, int numIterations) {
        Vector[] centroids = clusterInit.createCentroids(xs, numClusters);
        calculateCentroidPositions(centroids, xs, numIterations);

        HashMap<Vector, List<Vector>> closestVectorsToCluster = new HashMap<>();
        getClosestClustersToDataSet(centroids, xs, closestVectorsToCluster);
        return closestVectorsToCluster;
    }

    public double evaluateCost(HashMap<Vector, List<Vector>> clustering) {
        double cost = 0;

        int setSize = 0;
        for (Map.Entry<Vector, List<Vector>> entry: clustering.entrySet()) {
            for (Vector dataPoint: entry.getValue()) {
                cost += Math.pow((dataPoint.getSubtracted(entry.getKey()).getMagnitude()), 2);
                setSize++;
            }
        }
        cost *= 1./setSize;

        return cost;
    }

    private void calculateCentroidPositions(Vector[] centroids, Vector[] xs, int numIterations) {
        HashMap<Vector, List<Vector>> closestVectorsToCluster = new HashMap<>();
        for (int iter = 0; iter < numIterations; iter++) {
            getClosestClustersToDataSet(centroids, xs, closestVectorsToCluster);

            for (Map.Entry<Vector, List<Vector>> cluster: closestVectorsToCluster.entrySet()) {

                List<Vector> clusterPoints = cluster.getValue();
                Vector centroid = cluster.getKey();
                centroid.setToZeroVector();

                for (Vector point: clusterPoints) {
                    centroid.add(point);
                }
                centroid.scale(1./clusterPoints.size());

                cluster.setValue(new ArrayList<>());

            }
        }
    }

    private void getClosestClustersToDataSet(Vector[] centroids, Vector[] xs, HashMap<Vector, List<Vector>> closestVectorsToCluster) {
        for (Vector x : xs) {
            Vector closestCluster = getClosestClusterToDataPoint(x, centroids);
            if (!closestVectorsToCluster.containsKey(closestCluster))
                closestVectorsToCluster.put(closestCluster, new ArrayList<>());
            closestVectorsToCluster.get(closestCluster).add(x);
        }
    }

    private Vector getClosestClusterToDataPoint(Vector x, Vector[] centroids) {
        Vector closestCentroid = centroids[0];
        double closestDistance = x.getSubtracted(centroids[0]).getMagnitude();

        for (int i = 1; i < centroids.length; i++) {
            double distanceToDataPoint = x.getSubtracted(centroids[i]).getMagnitude();
            if (distanceToDataPoint < closestDistance) {
                closestCentroid = centroids[i];
                closestDistance = distanceToDataPoint;
            }
        }

        return closestCentroid;
    }


}
