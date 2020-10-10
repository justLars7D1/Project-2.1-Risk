package bot.NumericalAnalysis.LeastSquares;

import bot.Mathematics.Functions.ScalarFunction;
import bot.Mathematics.LinearAlgebra.LinearAlgebra;
import bot.Mathematics.LinearAlgebra.Matrix;
import bot.Mathematics.LinearAlgebra.Vector;

public class LeastSquares {

    private static LeastSquares instance;

    private LeastSquares() {
    }

    public static LeastSquares getInstance() {
        if (instance == null) {
            instance = new LeastSquares();
        }

        return instance;
    }

    public double[] computeCoefficients(Basis basis, ScalarFunction weight, double[] xs, double[] ys) {
        assert xs.length == ys.length;

        // We are going to compute S and r where S*c = r, and then solve for c.
        double[][] S = generateBasisMatrix(basis, weight, xs);
        double[] r = generateResultVector(basis, weight, xs, ys);

        Matrix mS = new Matrix(S);
        Vector vR = new Vector(r);

        Vector c = LinearAlgebra.solveSLE(mS, vR);

        return c.getCoordinates();
    }

    private double[][] generateBasisMatrix(Basis basis, ScalarFunction weight, double[] xs) {
        int numBasisFunctions = basis.getNumElements();
        ScalarFunction[] basisFunctions = basis.getBasisElements();

        double[][] basisMatrix = new double[numBasisFunctions][numBasisFunctions];

        for (int i = 0; i < numBasisFunctions; i++) {
            for (int j = 0; j < numBasisFunctions; j++) {
                for (double x : xs) {
                    basisMatrix[i][j] += weight.evaluate(x) * basisFunctions[i].evaluate(x) * basisFunctions[j].evaluate(x);
                }
            }
        }

        return basisMatrix;
    }

    private double[] generateResultVector(Basis basis, ScalarFunction weight, double[] xs, double[] ys) {
        assert xs.length == ys.length;

        int numDataPoints = xs.length;
        int numBasisFunctions = basis.getNumElements();
        ScalarFunction[] basisFunctions = basis.getBasisElements();

        double[] resultVector = new double[numBasisFunctions];

        for (int i = 0; i < numDataPoints; i++) {
            for (int j = 0; j < numBasisFunctions; j++) {
                resultVector[j] += weight.evaluate(xs[i]) * basisFunctions[j].evaluate(xs[i]) * ys[i];
            }
        }

        return resultVector;
    }

}
