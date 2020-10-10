package MachineLearning.NeuralNetwork;

import MachineLearning.NeuralNetwork.Activations.Sigmoid;
import MachineLearning.NeuralNetwork.Layers.Layer;
import MachineLearning.NeuralNetwork.Losses.MSE;
import MachineLearning.NeuralNetwork.Optimizers.SGD;
import bot.Mathematics.LinearAlgebra.Vector;

import java.util.List;

public class ModelTest extends Model {

    public ModelTest(int inputSize) {
        super(inputSize);
    }

    public static void main(String[] args) {

        // Example from https://mattmazur.com/2015/03/17/a-step-by-step-backpropagation-example/

        ModelTest m = new ModelTest(2);
        m.addLayer(2, new Sigmoid());
        m.addLayer(2, new Sigmoid());
        List<Layer> layers = m.getLayers();
        layers.get(0).getRepresentation().set(0, 0, .15);
        layers.get(0).getRepresentation().set(0, 1, .20);
        layers.get(0).getRepresentation().set(1, 0, .25);
        layers.get(0).getRepresentation().set(1, 1, .30);
        layers.get(0).getBias().set(0, .35);
        layers.get(0).getBias().set(1, .35);
        layers.get(1).getRepresentation().set(0, 0, .40);
        layers.get(1).getRepresentation().set(0, 1, .45);
        layers.get(1).getRepresentation().set(1, 0, .50);
        layers.get(1).getRepresentation().set(1, 1, .55);
        layers.get(1).getBias().set(0, .60);
        layers.get(1).getBias().set(1, .60);

        m.compile(new MSE(), new SGD(0.5), new String[] {"loss"});

        Vector[] xs = {new Vector(0.05, 0.10)};
        Vector[] ys = {new Vector(0.01, 0.99)};

        System.out.println(m.evaluate(xs[0]));
        System.out.println(new MSE().evaluate(m.evaluate(xs[0]), ys[0]) + "\n");

        m.train(xs, ys, 1, 1, 1);

        System.out.println(m + "\n");

        System.out.println(m.evaluate(xs[0]));

    }

    public List<Layer> getLayers() {
        return layers;
    }

}
