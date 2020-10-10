package bot.NumericalAnalysis.LeastSquares;

import bot.Mathematics.Functions.LinearEquation;
import bot.Mathematics.Functions.ScalarFunction;

import java.util.Arrays;

public class TestData {

    public static void main(String[] args) {

        final double[] xs = {0, 1, 2, 3, 4, 5};
        final double[] ys = {-1, 4, 3, 0, 2, 3};

        ScalarFunction[] funcs = getFunctions();

        Basis basis = new Basis(funcs);
        ScalarFunction weight = funcs[0];

        LeastSquares leastSquares = LeastSquares.getInstance();
        double[] c = leastSquares.computeCoefficients(basis, weight, xs, ys);

        LinearEquation equation = new LinearEquation(basis, c);

        System.out.println(equation);

        System.out.println(Arrays.toString(equation.evaluate(xs)));
        System.out.println(String.format("RMSE: %f", equation.calculateRMSE(xs, ys)));

    }

    private static ScalarFunction[] getFunctions() {
        return new ScalarFunction[] {
                x -> 1,
                x -> x
        };
    }

}
