package me.hardstyl3r.neuralnet;

public class Main {

    public static void main(String[] args) {
        double[][] trainX, trainY, testX, testY;
        try {
            DataLoader.loadAllFromDirectory("./pomiary");
            trainX = DataLoader.getTrainX();
            trainY = DataLoader.getTrainY();
            testX = DataLoader.getTestX();
            testY = DataLoader.getTestY();
            System.out.println("Próbki treningowe: " + trainX.length);
            System.out.println("Próbki testowe (dynamiczne): " + testX.length);

            long now = System.currentTimeMillis();
            NeuralNetwork network = new NeuralNetwork();
            int epochs = 300;
            double learningRate = 0.00001;
            network.train(trainX, trainY, epochs, learningRate);

            double totalBefore = 0;
            double totalAfter = 0;

            for (int i = 0; i < testX.length; i++) {
                double[] input = testX[i];
                double[] label = testY[i];

                double x = input[16] * 10000;
                double y = input[17] * 10000;

                double refX = label[0] * 10000;
                double refY = label[1] * 10000;

                double distBefore = distance(x, y, refX, refY);
                totalBefore += distBefore;

                double[] prediction = network.predict(input);
                double predX = prediction[0] * 10000;
                double predY = prediction[1] * 10000;

                double distAfter = distance(predX, predY, refX, refY);
                totalAfter += distAfter;
            }
            System.out.printf("Średni błąd przed korekcją: %.3f mm%n", totalBefore / testX.length);
            System.out.printf("Średni błąd po korekcji:    %.3f mm%n", totalAfter / testX.length);
            System.out.printf("Trening zakończony w:       %d ms", (System.currentTimeMillis() - now));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
