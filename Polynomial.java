public class Polynomial {   
    private double[] coef;
    
    public Polynomial() {
        this.coef = new double[1];
    }

    public Polynomial(double[] coefficients) {
        this.coef = new double[coefficients.length];
        for (int i = 0; i < coefficients.length; i++) {
            this.coef[i] = coefficients[i];
        }
    }

    public Polynomial add(Polynomial p) {
        int selfDegree = this.coef.length;
        int pDegree = p.coef.length;

        int maxDegree = Math.max(selfDegree, pDegree);
        double[] newCoef = new double[maxDegree];
        for (int i = 0; i < maxDegree; i++) {
            double selfCoef = i < selfDegree ? this.coef[i] : 0;
            double pCoef = i < pDegree ? p.coef[i] : 0;
            newCoef[i] = selfCoef + pCoef;
        }

        return new Polynomial(newCoef);
    }

    public double evaluate(double x) {
        int selfDegree = this.coef.length;

        double accumulator = 0;

        for (int i = 0; i < selfDegree; i ++) {
            accumulator += this.coef[i] * Math.pow(x, i);
        }

        return accumulator;

    }

    public boolean hasRoot(double x) {
        return this.evaluate(x) == 0;
    }
}
