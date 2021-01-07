
package bot.MachineLearning.NeuralNetwork;

import java.io.*;
import java.util.*;

public class TDMetricCollector {

    public static final String[] EXISTING_METRICS = {"alpha", "lambda", "winningChanceThreshold", "randomChanceThreshold", "armiesFeatureWeight", "territoryFeatureWeight", "enemyReinforcementFeatureWeight","bestEnemyFeatureWeight","hinterlandFeatureWeight", "stateValue", "turnsUntilWin"};

    private final HashMap<String, List<Double>> metrics;

    public TDMetricCollector() {
        this.metrics = new HashMap<>();
    }

    public List<Double> getMetric(String metricName) {
        String name = getMetricName(metricName);
        if (name.equals("")) {
            try {
                throw new UnsupportedMetricException();
            } catch (UnsupportedMetricException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        return metrics.get(name);
    }

    public void addToMetric(String metricName, double value) {
        List<Double> values = getMetric(metricName);
        values.add(value);
    }

    public void enableMetric(String metricName) {
        String name = getMetricName(metricName);
        if (!name.equals("")) {
            addMetric(name);
        } else {
            try {
                throw new UnsupportedMetricException();
            } catch (UnsupportedMetricException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    private void addMetric(String name) {
        if (!metrics.containsKey(name)) {
            metrics.put(name, new ArrayList<>());
        }
    }

    private String getMetricName(String metricName) {
        switch (metricName.toLowerCase()) {
            case "alpha":
                return "alpha";
            case "lambda":
                return "lambda";
            case "winningChanceThreshold":
                return "winningchancethreshold";
            case "randomChanceThreshold":
                return "randomchancethreshold";
            case "armiesFeatureWeight":
                return "armiesfeatureweight";
            case "territoryFeatureWeight":
                return "territoryfeatureweight";
            case "enemyReinforcementFeatureWeight":
                return "enemyreinforcementfeatureweight";
            case "bestEnemyFeatureWeight":
                return "bestenemyfeatureweight";
            case "hinterlandFeatureWeight":
                return "hinterlandfeatureweight";
            case "StateValues":
                return "statevalues";
            case "turnsUntilWin":
                return "turnsuntilwin";
            default:
                return "";
        }
    }

    public void saveToFile(String fileLocation) {
        try {

            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileLocation)));
            List<String> names = new ArrayList<>();
            List<List<Double>> data = new ArrayList<>();
            for (Map.Entry<String, List<Double>> entry: metrics.entrySet()) {
                names.add(entry.getKey());
                data.add(entry.getValue());
            }

            writer.print("iter,");
            for (int i = 0; i < names.size(); i++) {
                writer.print(names.get(i));
                if (i+1 != names.size()) writer.print(",");
                else writer.println();
            }

            for (int i = 0; i < data.get(0).size(); i++) {
                writer.print((i+1) + ",");
                for (int j = 0; j < data.size(); j++) {
                    writer.print(data.get(j).get(i));
                    if (j+1 != data.size()) writer.print(",");
                    else writer.println();
                }
            }

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public HashMap<String, List<Double>> getMetrics() {
        return metrics;
    }

    static class UnsupportedMetricException extends Exception {
    }

}