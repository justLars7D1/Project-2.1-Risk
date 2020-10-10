package bot.Mathematics.LinearAlgebra;

public class Vector implements Cloneable {

    private final double[] coordinates;

    public Vector(final double ... coordinates) {
        this.coordinates = coordinates;
    }

    public Vector(final int dimensions) {
        assert dimensions >= 1;
        coordinates = new double[dimensions];
    }

    public double get(int i) {
        return coordinates[i];
    }

    public void set(int i, double v) {
        coordinates[i] = v;
    }

    public int getDimensions() {
        return coordinates.length;
    }

    public double getVectorSum() {
        double sum = 0;
        for (double val: coordinates) sum += val;
        return sum;
    }

    public void subtract(final Vector otherVector) {
        int dimensions = getDimensions();
        assert dimensions == otherVector.getDimensions();

        double[] otherCoords = otherVector.getCoordinates();
        for (int i = 0; i < dimensions; i++) {
            coordinates[i] -= otherCoords[i];
        }
    }

    public void add(final Vector otherVector) {
        int dimensions = getDimensions();
        assert dimensions == otherVector.getDimensions();

        double[] otherCoords = otherVector.getCoordinates();
        for (int i = 0; i < dimensions; i++) {
            coordinates[i] += otherCoords[i];
        }
    }

    public void add(final double value) {
        int dimensions = getDimensions();
        for (int i = 0; i < dimensions; i++) {
            coordinates[i] += value;
        }
    }

    public void multiply(final Vector otherVector) {
        int dimensions = getDimensions();
        assert dimensions == otherVector.getDimensions();

        double[] otherCoords = otherVector.getCoordinates();
        for (int i = 0; i < dimensions; i++) {
            coordinates[i] *= otherCoords[i];
        }
    }

    public void divide(final Vector otherVector) {
        int dimensions = getDimensions();
        assert dimensions == otherVector.getDimensions();

        double[] otherCoords = otherVector.getCoordinates();
        for (int i = 0; i < dimensions; i++) {
            coordinates[i] /= otherCoords[i];
        }
    }

    public void scale(final double scaleFactor) {
        int dimensions = getDimensions();
        for (int i = 0; i < dimensions; i++) {
            coordinates[i] *= scaleFactor;
        }
    }

    public void exponentScale(double exp) {
        int dimensions = getDimensions();
        for (int i = 0; i < dimensions; i++) {
            coordinates[i] = Math.pow(coordinates[i], exp);
        }
    }

    public Vector getAdded(Vector otherVector) {
        Vector copyOfCurrentVector = (Vector) clone();
        copyOfCurrentVector.add(otherVector);
        return copyOfCurrentVector;
    }

    public Vector getAdded(double value) {
        Vector copyOfCurrentVector = (Vector) clone();
        copyOfCurrentVector.add(value);
        return copyOfCurrentVector;
    }

    public Vector getSubtracted(Vector otherVector) {
        Vector copyOfCurrentVector = (Vector) clone();
        copyOfCurrentVector.subtract(otherVector);
        return copyOfCurrentVector;
    }

    public void setToZeroVector() {
        for (int i = 0; i < getDimensions(); i++) {
            coordinates[i] = 0;
        }
    }

    public Vector getMultiplied(Vector otherVector) {
        Vector copyOfCurrentVector = (Vector) clone();
        copyOfCurrentVector.multiply(otherVector);
        return copyOfCurrentVector;
    }

    public Vector getDivided(Vector otherVector) {
        Vector copyOfCurrentVector = (Vector) clone();
        copyOfCurrentVector.divide(otherVector);
        return copyOfCurrentVector;
    }

    public Vector getScaled(final double scaleFactor) {
        Vector copy = (Vector) clone();
        copy.scale(scaleFactor);
        return copy;
    }

    public Vector getExponentScaled(final double exp) {
        Vector copy = (Vector) clone();
        copy.exponentScale(exp);
        return copy;
    }

    public Matrix getTransposed() {
        Matrix o = new Matrix(this);
        return o.getTransposed();
    }

    public double getDotProduct(Vector otherVector) {
        int dimensions = getDimensions();
        assert dimensions == otherVector.getDimensions();

        double[] otherCoords = otherVector.getCoordinates();
        double dotProduct = 0;
        for (int i = 0; i < dimensions; i++) {
            dotProduct += coordinates[i] * otherCoords[i];
        }

        return dotProduct;
    }

    public double getMagnitude() {
        double magnitudeSquared = 0;

        for (double coordinate: coordinates) {
            magnitudeSquared += Math.pow(coordinate, 2);
        }

        return Math.sqrt(magnitudeSquared);
    }

    public void normalize() {
        double magnitude = getMagnitude();

        for (int i = 0; i < getDimensions(); i++) {
            coordinates[i] /= magnitude;
        }
    }

    public Vector getNormalized() {
        Vector copyOfCurrentVector = (Vector) clone();
        double[] copyVectorCoords = copyOfCurrentVector.getCoordinates();
        double magnitude = getMagnitude();

        for (int i = 0; i < getDimensions(); i++) {
            copyVectorCoords[i] /= magnitude;
        }

        return copyOfCurrentVector;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public double getPNorm(double p) {
        double norm = 0;
        for (double coordinate: coordinates) {
            norm += Math.pow(Math.abs(coordinate), p);
        }
        return Math.pow(norm, 1/p);
    }

    public double getInfinityNorm() {
        double norm = 0;
        for (double coordinate: coordinates) {
            double absCoord = Math.abs(coordinate);
            if (absCoord > norm) {
                norm = absCoord;
            }
        }
        return norm;
    }

    @Override
    public Object clone() {
        return new Vector(coordinates.clone());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector)) return false;

        Vector otherVector = (Vector) obj;
        int numDims = getDimensions();
        if (otherVector.getDimensions() != numDims) return false;

        double[] otherVectorCoords = otherVector.getCoordinates();
        for (int i = 0; i < numDims; i++) {
            if (otherVectorCoords[i] != coordinates[i]) return false;
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder vectorString = new StringBuilder("(");

        int numDims = getDimensions();
        for (int i = 0; i < numDims; i++) {
            vectorString.append(coordinates[i]);
            if (i + 1 < numDims) {
                vectorString.append(", ");
            } else {
                vectorString.append(")");
            }
        }

        return vectorString.toString();
    }

}
