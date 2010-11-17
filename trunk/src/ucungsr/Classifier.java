/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ucungsr;

/**
 *
 * @author mauricio
 */
public class Classifier {

    private double[] probabilities = null;
    private int mostProbable;
    private Speaker[] speakers = null;
    private Speaker speaker = null;

    public Classifier() {
    }

    public Classifier(Speaker _speaker, Speaker[] _speakers) {
        speaker = _speaker;
        speakers = _speakers;
    }

    public int getMostProbableId() {
        mostProbable = -1;
        double prob = 0.0;
        probabilities = new double[speakers.length];
        for (int i = 0; i < speakers.length; i++) {
            probabilities[i] = calculate(speaker, speakers[i]);
            if (prob <= probabilities[i]) {
                mostProbable = i;
                prob = probabilities[i];
            }
        }
        return speakers[mostProbable].getId();
    }

    public double getProbability() {
        return probabilities[mostProbable];
    }

    private double calculate(Speaker spk1, Speaker spk2) {
        int D = spk1.getMeanVector().length;

        double detSigma = 1;
        double d2 = 0;
        for (int i = 1; i < D; i++) {
            d2 += Math.pow(spk1.getMeanVector()[i] - spk2.getMeanVector()[i], 2) / spk2.getVarianceVector()[i];
            detSigma *= spk2.getVarianceVector()[i];
        }

        double b = Math.pow(2 * Math.PI, D - 1) * detSigma;
        return Math.sqrt(Math.exp(-d2) / b);
    }
}
