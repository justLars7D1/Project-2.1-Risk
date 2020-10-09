package bot.NumericalAnalysis.LeastSquares;

import bot.Mathematics.Functions.ScalarFunction;

public class Basis {

    private final ScalarFunction[] basisElements;

    public Basis(final ScalarFunction ... functions) {
        this.basisElements = functions;
    }

    public int getNumElements() {
        return basisElements.length;
    }

    public ScalarFunction[] getBasisElements() {
        return basisElements;
    }
}
