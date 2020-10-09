package bot.Mathematics.Functions;

import bot.NumericalAnalysis.LeastSquares.Basis;

public class LinearEquation implements ScalarFunction {

    private final Basis basis;
    private final double[] coefficients;

    public LinearEquation(final Basis basis, final double[] coefficients) {
        assert basis.getNumElements() == coefficients.length;
        this.basis = basis;
        this.coefficients = coefficients;
    }

    public double[] evaluate(final double[] xs) {
        int numDataPoints = xs.length;

        double[] ys = new double[numDataPoints];
        for (int i = 0; i < numDataPoints; i++) {
            ys[i] = evaluate(xs[i]);
        }

        return ys;
    }

    @Override
    public double evaluate(final double x) {
        double y = 0;
        int numBasisFunctions = basis.getNumElements();
        ScalarFunction[] basisFunctions = basis.getBasisElements();

        for (int i = 0; i < numBasisFunctions; i++) {
            y += coefficients[i] * basisFunctions[i].evaluate(x);
        }

        return y;
    }

    public double calculateRMSE(final double[] xs, final double[] ys) {
        assert xs.length == ys.length;

        double MSE = 0;
        for (int i = 0; i < xs.length; i++) {
            MSE += Math.pow(ys[i] - evaluate(xs[i]), 2);
        }

        return Math.sqrt(MSE);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("l(x) = ");

        int numComponents = basis.getNumElements();
        for (int i = 0; i < numComponents; i++) {
            res.append(coefficients[i]).append("*f[").append(i).append("](x)");
            if (i + 1 != numComponents) {
                res.append(" + ");
            }
        }

        return res.toString();
    }
}
