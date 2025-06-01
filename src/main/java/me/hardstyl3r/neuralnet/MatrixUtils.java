package me.hardstyl3r.neuralnet;

import java.util.Random;

public class MatrixUtils {
    private static final Random rng = new Random(2905);

    public static double[] dot(double[][] weights, double[] input) {
        double[] result = new double[weights.length];
        for (int i = 0; i < weights.length; i++) {
            result[i] = 0;
            for (int j = 0; j < input.length; j++) {
                result[i] += weights[i][j] * input[j];
            }
        }
        return result;
    }

    public static double[] add(double[] a, double[] b) {
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++)
            result[i] = a[i] + b[i];
        return result;
    }

    public static double[][] randomMatrix(int rows, int cols) {
        double[][] matrix = new double[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                matrix[i][j] = rng.nextDouble() - 0.5; // [-0.5, 0.5]
        return matrix;
    }

    public static double[] randomVector(int size) {
        double[] vec = new double[size];
        for (int i = 0; i < size; i++)
            vec[i] = rng.nextDouble() - 0.5;
        return vec;
    }
}
