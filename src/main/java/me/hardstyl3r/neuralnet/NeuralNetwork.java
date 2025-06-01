package me.hardstyl3r.neuralnet;

public class NeuralNetwork {
    public Layer[] layers;

    public NeuralNetwork() {
        layers = new Layer[] {
                new Layer(20, 20, "relu"),
                new Layer(20, 20, "relu"),
                new Layer(20, 20, "relu"),
                new Layer(20, 2, "linear")
        };
    }

    public double[] predict(double[] input) {
        double[] output = input;
        for (Layer layer : layers)
            output = layer.forward(output);
        return output;
    }

    public void train(double[][] X, double[][] Y, int epochs, double learningRate) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            double loss = 0;
            for (int i = 0; i < X.length; i++) {
                double[] output = predict(X[i]);

                double[] error = new double[output.length];
                for (int j = 0; j < output.length; j++) {
                    error[j] = output[j] - Y[i][j];
                    loss += error[j] * error[j];
                }

                for (int j = layers.length - 1; j >= 0; j--) {
                    error = layers[j].backward(error, learningRate);
                }
            }
            System.out.printf("Epoch %d, Loss: %.6f%n", epoch, loss / X.length);
        }
    }
}
