package me.hardstyl3r.neuralnet;

public class Layer {
    public int inputSize, outputSize;
    public double[][] weights;
    public double[] biases;
    public double[] input, output, outputRaw;
    public String activation;

    public Layer(int inputSize, int outputSize, String activation) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.activation = activation;
        this.weights = MatrixUtils.randomMatrix(outputSize, inputSize);
        this.biases = MatrixUtils.randomVector(outputSize);
    }

    public double[] forward(double[] input) {
        this.input = input;
        this.outputRaw = MatrixUtils.add(MatrixUtils.dot(weights, input), biases);
        this.output = switch (activation) {
            case "relu" -> Activation.relu(outputRaw);
            case "linear" -> Activation.identity(outputRaw);
            default -> throw new IllegalArgumentException("Unknown activation: " + activation);
        };
        return this.output;
    }

    public double[] backward(double[] outputError, double learningRate) {
        double[] dActivation = switch (activation) {
            case "relu" -> Activation.reluDerivative(outputRaw);
            case "linear" -> Activation.identityDerivative(outputRaw);
            default -> throw new IllegalArgumentException("Unknown activation: " + activation);
        };

        double[] delta = new double[outputSize];
        for (int i = 0; i < outputSize; i++)
            delta[i] = outputError[i] * dActivation[i];

        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                weights[i][j] -= learningRate * delta[i] * input[j];
            }
            biases[i] -= learningRate * delta[i];
        }

        double[] inputError = new double[inputSize];
        for (int i = 0; i < inputSize; i++) {
            inputError[i] = 0;
            for (int j = 0; j < outputSize; j++) {
                inputError[i] += weights[j][i] * delta[j];
            }
        }

        return inputError;
    }
}
