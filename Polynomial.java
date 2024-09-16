public class Polynomial {   
    private final double[] coefficients;
    
    public Polynomial() {
        this.coefficients = new double[1];
    }

    public Polynomial(double[] coefficients) {
        this.coefficients = new double[coefficients.length];
        System.arraycopy(coefficients, 0, this.coefficients, 0, coefficients.length);
    }

    public Polynomial add(Polynomial p) {
        int selfDegree = this.coefficients.length;
        int pDegree = p.coefficients.length;

        int maxDegree = Math.max(selfDegree, pDegree);
        double[] newCoef = new double[maxDegree];
        for (int i = 0; i < maxDegree; i++) {
            double selfCoef = i < selfDegree ? this.coefficients[i] : 0;
            double pCoef = i < pDegree ? p.coefficients[i] : 0;
            newCoef[i] = selfCoef + pCoef;
        }

        return new Polynomial(newCoef);
    }

    public double evaluate(double x) {
        int selfDegree = this.coefficients.length;

        double accumulator = 0;

        for (int i = 0; i < selfDegree; i ++) {
            accumulator += this.coefficients[i] * Math.pow(x, i);
        }

        return accumulator;

    }

    public boolean hasRoot(double x) {
        return this.evaluate(x) == 0;
    }
}
