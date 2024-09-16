public class Polynomial {
    public final double[] coefficients;

    /**
     * Creates a new Polynomial with a single coefficient of 0
     */
    public Polynomial() {
        this.coefficients = new double[1];
        this.coefficients[0] = 0;
    }

    /**
     * Creates a new Polynomial with the given coefficients
     * @param coefficients the coefficients of the polynomial
     *                     the first element is the coefficient of x^0
     *                     the second element is the coefficient of x^1
     *                     and so on
     */
    public Polynomial(double[] coefficients) {
        this.coefficients = new double[coefficients.length];
        System.arraycopy(coefficients, 0, this.coefficients, 0, coefficients.length);
    }

    /**
     * Adds the given polynomial to this polynomial. The sum is carried out by adding the coefficients of the same degree, kind of like in the ring of polynomials.
     * @param p the polynomial to add
     * @return a new polynomial that is the sum of this polynomial and the given polynomial
     */
    public Polynomial add(Polynomial p) {
        int selfDegree = this.coefficients.length;
        int pDegree = p.coefficients.length;

        int maxDegree = Math.max(selfDegree, pDegree);
        double[] newCoef = new double[maxDegree];
        for (int i = 0; i < maxDegree; i ++) {
            double selfCoefHere;
            double pCoefHere;
            if (i < selfDegree) {
                selfCoefHere = this.coefficients[i];
            } else {
                selfCoefHere = 0;
            }

            if (i < pDegree) {
                pCoefHere = p.coefficients[i];
            } else {
                pCoefHere = 0;
            }
            newCoef[i] = selfCoefHere + pCoefHere;
        }

        return new Polynomial(newCoef);
    }

    /**
     * Evaluates the polynomial at the given value of x
     * @param x the value of x to evaluate the polynomial at
     * @return the value of the polynomial at x
     */
    public double evaluate(double x) {
        int selfDegree = this.coefficients.length;

        double accumulator = 0;

        for (int i = 0; i < selfDegree; i ++) {
            accumulator += this.coefficients[i] * Math.pow(x, i);
        }

        return accumulator;

    }

    /**
     * Checks if the polynomial has a root at the given value of x. A root
     * of a polynomial is a value of x such that the polynomial evaluates to 0.
     * @param x the value of x to check for a root
     * @return true if the polynomial has a root at x, false otherwise
     */
    public boolean hasRoot(double x) {
        return this.evaluate(x) == 0;
    }
}
