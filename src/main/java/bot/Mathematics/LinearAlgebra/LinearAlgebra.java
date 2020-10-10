package bot.Mathematics.LinearAlgebra;

public class LinearAlgebra {

    public static Vector solveSLE(Matrix m, Vector v) {
        double[][] A = m.getGrid();
        double[] b = v.getCoordinates();
        double[] x = GaussianElimination.lsolve(A, b);
        return new Vector(x);
    }

    public static void main(String[] args) {
        Vector b = new Vector(0.5, -1.0, -0.5);
        double[][] mData = {{0.5, -1, 0}, {-2, 3.5, -2}, {0, -1, 1.5}};
        Matrix m = new Matrix(mData);

        Vector v = m.multiply(b);
        System.out.println(v);
        System.out.println(v.getScaled(1/v.getInfinityNorm()));

    }

}
