package MachineLearning.NeuralNetwork;

import MachineLearning.NeuralNetwork.Activations.Sigmoid;
import MachineLearning.NeuralNetwork.Losses.MSE;
import MachineLearning.NeuralNetwork.Optimizers.SGD;
import bot.Mathematics.LinearAlgebra.Vector;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        Model m = new Model(2);
        m.addLayer(2, new Sigmoid());
        m.addLayer(1, new Sigmoid());

        m.compile(new MSE(), new SGD(1), new String[] {"loss", "accuracy"});

//        XOR:
//        0	0	0
//        0	1	1
//        1	0	1
//        1	1	0

        List<Vector> input = new ArrayList<>();
        input.add(new Vector(0, 0));
        input.add(new Vector(0, 1));
        input.add(new Vector(1, 0));
        input.add(new Vector(1, 1));

        Vector o1 = new Vector(1);
        Vector o2 = new Vector(1);
        Vector o3 = new Vector(1);
        Vector o4 = new Vector(1);

        o2.set(0, 1);
        o3.set(0, 1);

        List<Vector> output = new ArrayList<>();
        output.add(o1);
        output.add(o2);
        output.add(o3);
        output.add(o4);

        MetricCollector collector = m.train(input.toArray(new Vector[0]), output.toArray(new Vector[0]), 1, 1000, 1);
        collector.saveToFile("D:\\Projects\\JavaAlgorithms\\src\\MachineLearning\\NeuralNetwork\\test.txt");

        m.gradientChecking(input.get(0), output.get(0));

        System.out.println("\nPredicted: " + m.evaluate(input));

        System.out.println("\n" + m);


    }

}
