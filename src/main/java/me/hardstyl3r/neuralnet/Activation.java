package me.hardstyl3r.neuralnet;

public class Activation {
    public static double relu(double x) {
        return Math.max(0, x);
    }

    public static double reluDerivative(double x) {
        return x > 0 ? 1 : 0;
    }

    public static double[] relu(double[] x) {
        double[] result = new double[x.length];
        for (int i = 0; i < x.length; i++)
            result[i] = relu(x[i]);
        return result;
    }

    public static double[] reluDerivative(double[] x) {
        double[] result = new double[x.length];
        for (int i = 0; i < x.length; i++)
            result[i] = reluDerivative(x[i]);
        return result;
    }

    public static double[] identity(double[] x) {
        return x;
    }

    public static double[] identityDerivative(double[] x) {
        double[] result = new double[x.length];
        for (int i = 0; i < x.length; i++)
            result[i] = 1;
        return result;
    }
}
