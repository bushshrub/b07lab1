import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class Polynomial {
    public double[] nonZeroCoefficients;
    public int[] exponents;

    /**
     * Creates a new Polynomial with a single coefficient of 0
     */
    public Polynomial() {
        this.nonZeroCoefficients = null;
        this.exponents = null;
    }

    /**
     * Creates a new Polynomial with the given coefficients
     *
     * @param exponents           The exponents of the polynomial.
     * @param nonZeroCoefficients the coefficients of the polynomial.
     *                            Note that this has to line up with
     *                            the exponents of the polynomial as specified.
     * @throws IllegalArgumentException If the length of the exponents and nonzero coefficients do not match up
     */
    public Polynomial(double[] nonZeroCoefficients, int[] exponents) {
        if (exponents.length != nonZeroCoefficients.length) {
            throw new IllegalArgumentException("There must be exactly as many exponents as non zero coefficients!");
        }
        this.nonZeroCoefficients = new double[nonZeroCoefficients.length];
        this.exponents = new int[exponents.length];

        System.arraycopy(nonZeroCoefficients, 0, this.nonZeroCoefficients, 0, nonZeroCoefficients.length);
        System.arraycopy(exponents, 0, this.exponents, 0, exponents.length);
    }

    /**
     * Creates a new Polynomial from a hashmap representation. Note that we have assumed
     * your map representation contains no redundant entries (i.e. no entry where the value is 0)
     * @param mapRepresentation The HashMap representation of the polynomial. The keys are the exponents, the values are the coefficients.
     *
     */
    public Polynomial(HashMap<Integer, Double> mapRepresentation) {
        int[] exponents = new int[mapRepresentation.size()];
        double[] coefficients = new double[mapRepresentation.size()];
        int i = 0;
        for (Map.Entry<Integer, Double> entry : mapRepresentation.entrySet()) {
            exponents[i] = entry.getKey();
            coefficients[i] = entry.getValue();
            i++;
        }
        this.exponents = exponents;
        this.nonZeroCoefficients = coefficients;
    }

    /**
     * Constructs a polynomial given a file.
     * @param polynomialFile
     */
    public Polynomial(File polynomialFile) {
        try {
            Scanner scanner = new Scanner(polynomialFile);
            String poly = scanner.nextLine();
            HashMap<Integer, Double> mapRepresentation = parsePolynomialString(poly).convertToMap();

            int[] exponents = new int[mapRepresentation.size()];
            double[] coefficients = new double[mapRepresentation.size()];
            int i = 0;
            for (Map.Entry<Integer, Double> entry : mapRepresentation.entrySet()) {
                exponents[i] = entry.getKey();
                coefficients[i] = entry.getValue();
                i++;
            }
            this.exponents = exponents;
            this.nonZeroCoefficients = coefficients;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static Polynomial parsePolynomialString(String polyString) {
        HashMap<Integer, Double> coefficientMap = new HashMap<>();
        polyString = polyString.replaceAll("[-]", "+-");

        String[] splices = polyString.split("[+]");
        for (String spl : splices) {
            if (!spl.contains("x")) {
                coefficientMap.put(0, Double.parseDouble(spl));
                continue;
            }
            String[] coefficientAndExponent = spl.split("x");
            // coefficientAndExponent is definitely length 2
            // exponent is at pos 1, coefficient is pos 0
            coefficientMap.put(Integer.parseInt(coefficientAndExponent[1]), Double.parseDouble(coefficientAndExponent[0]));
        }
        return new Polynomial(coefficientMap);
    }

    /**
     * Adds the given polynomial to this polynomial.
     * The sum is carried out by adding the coefficients of the same degree,
     * kind of like in the ring of polynomials.
     *
     * @param p the polynomial to add
     * @return a new polynomial that is the sum of this polynomial and the given polynomial
     */
    public Polynomial add(Polynomial p) {
        // Assumptions: exponents has the same length as nonzero coefficients
        HashMap<Integer, Double> selfExponentsCoefficientMap = this.convertToMap();

        HashMap<Integer, Double> pExponentsCoefficientMap = p.convertToMap();

        HashMap<Integer, Double> resultingPolynomial = new HashMap<>(selfExponentsCoefficientMap);
        pExponentsCoefficientMap.forEach((key, value) -> {
            if (resultingPolynomial.containsKey(key)) {
                resultingPolynomial.compute(key, (k, existingCoefficient) -> existingCoefficient + value);
            } else {
                resultingPolynomial.put(key, value);
            }
        });


        List<Integer> zeroCoefficients = resultingPolynomial.entrySet().stream().filter(entry -> entry.getValue() == 0).map(Map.Entry::getKey).toList();
        zeroCoefficients.forEach(resultingPolynomial::remove);

        return new Polynomial(resultingPolynomial);

    }


    /**
     * Multiplies this polynomial with a different polynomial
     * @param p The other polynomial to multiply
     * @return The result of multiplication
     */
    public Polynomial multiply(Polynomial p) {
        HashMap<Integer, Double> thisExpCoefficientMap = this.convertToMap();
        HashMap<Integer, Double> pExpCoefficientMap = p.convertToMap();

        HashMap<Integer, Double> resultExpCoefficientMap = new HashMap<>();
        for (Map.Entry<Integer, Double> pExponentCoefficientPair : pExpCoefficientMap.entrySet()) {

            HashMap<Integer, Double> resultOfThisMultiplication = new HashMap<>();

            for (Map.Entry<Integer, Double> thisExponentCoefficientPair: thisExpCoefficientMap.entrySet()) {
                resultOfThisMultiplication.put(pExponentCoefficientPair.getKey() + thisExponentCoefficientPair.getKey(), pExponentCoefficientPair.getValue() * thisExponentCoefficientPair.getValue());
            }

            for (Map.Entry<Integer, Double> entry : resultOfThisMultiplication.entrySet()) {
                Integer key = entry.getKey();
                Double value = entry.getValue();
                resultExpCoefficientMap.computeIfPresent(key, (k, presentVal) -> value + presentVal);
                resultExpCoefficientMap.putIfAbsent(key, value);
            }
        }
        return new Polynomial(resultExpCoefficientMap);
    }

    public HashMap<Integer, Double> convertToMap() {
        HashMap<Integer, Double> exponentCoefficientMap = new HashMap<>();
        for (int i = 0; i < this.getNumberNonZeroCoefficients(); i++) {
            exponentCoefficientMap.put(this.exponents[i], this.nonZeroCoefficients[i]);
        }
        return exponentCoefficientMap;
    }

    /**
     * Get the number of nonzero coefficients. This must be equal
     * to the number of exponents.
     *
     * @return Number of nonzero coefficients
     */
    public int getNumberNonZeroCoefficients() {
        return this.nonZeroCoefficients.length;
    }

    /**
     * Evaluates the polynomial at the given value of x
     *
     * @param x the value of x to evaluate the polynomial at
     * @return the value of the polynomial at x
     */
    public double evaluate(double x) {
        double accumulator = 0;
        for (Map.Entry<Integer, Double> entry : this.convertToMap().entrySet()) {
            accumulator += entry.getValue() * Math.pow(x, entry.getKey());
        }

        return accumulator;

    }

    /**
     * Checks if the polynomial has a root at the given value of x. A root
     * of a polynomial is a value of x such that the polynomial evaluates to 0.
     *
     * @param x the value of x to check for a root
     * @return true if the polynomial has a root at x, false otherwise
     */
    public boolean hasRoot(double x) {
        return this.evaluate(x) == 0;
    }

    @Override
    public String toString() {
        // convert polynomial to string and print it, sorted in the order of exponents
        TreeMap<Integer, Double> sortedMap = new TreeMap<>(this.convertToMap());
        StringBuilder polynomialString = new StringBuilder();
        for (Map.Entry<Integer, Double> entry : sortedMap.entrySet()) {
            // if coefficient is negative, we don't want to print the + sign
            if (entry.getValue() < 0) {
                polynomialString.append(entry.getValue());
            } else {
                polynomialString.append("+");
            }

            // if the coefficient is 1, we don't want to print it
            if (entry.getValue() >= 0 && entry.getValue() != 1) {
                polynomialString.append(entry.getValue());
            }
            // if the exponent is 0, we don't want to print x
            if (entry.getKey() != 0) {
                polynomialString.append("x");
                // if the exponent is 1, we don't want to print it
                if (entry.getKey() != 1) {
                    polynomialString.append(entry.getKey());
                }
            }

        }
        // remove the first + sign
        if (!polynomialString.isEmpty() && polynomialString.charAt(0) == '+') {
            polynomialString.deleteCharAt(0);
        }

        return polynomialString.toString();
    }

    public void saveToFile(String fileName) {
        try {
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write(this.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
