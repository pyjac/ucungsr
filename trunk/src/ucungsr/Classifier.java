/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ucungsr;

/**
 *
 * @author mauricio
 */
public final class Classifier {

    private double[] probabilities = null;
    private double[] sortedProbabilities = null;
    private int mostProbable;
    private Speaker[] speakers = null;
    private Speaker speaker = null;
    private double likehood;
    private double treshold;

    public Classifier() {
    }

    public Classifier(Speaker _speaker, Speaker[] _speakers) {
        speaker = _speaker;
        speakers = _speakers;
        classify();
    }

    public void setTreshold(double treshold) {
        this.treshold = treshold;
    }

    public void classify() {
        mostProbable = -1;
        double prob = 0.0;
        probabilities = new double[speakers.length];
        likehood = 0.0;
        for (int i = 0; i < speakers.length; i++) {
            probabilities[i] = calculate(speaker, speakers[i]);
            likehood +=probabilities[i];
            if (probabilities[i] > prob) {
                mostProbable = i;
                prob = probabilities[i];
            }
        }
        likehood /= speakers.length;
        sortProbabilities();
    }

    public boolean inSet(){
        return (likehood > treshold);
    }

    private void sortProbabilities() {
        double[] tmpSortedProbabilities = probabilities;
        sortedProbabilities = new double[probabilities.length];
        java.util.Arrays.sort(tmpSortedProbabilities);
        for (int i = 0; i < probabilities.length; i++) {
            sortedProbabilities[i] = tmpSortedProbabilities[probabilities.length - i - 1];
        }
    }

    private double calculate(Speaker spkX, Speaker spkRef) {
//        System.out.println("calculating '" + spk1.getName() + "' against '" + spk2.getName() + "'...");
//        System.out.println(spk2.toString());
        double D = (double)spkRef.getMeanVector().length;
        double detSigma = 1.0;
        double d2 = 0.0;
        for (int i = 1; i < D; i++) {
            d2 += Math.pow(spkX.getMeanVector()[i] - spkRef.getMeanVector()[i], 2) / spkRef.getVarianceVector()[i];
            detSigma *= spkRef.getVarianceVector()[i];
        }
        
        double prob = Math.sqrt(Math.exp(-d2) / (Math.pow(2 * Math.PI, D-1) * detSigma));
//        System.out.println("Probability:" + Double.toString(prob));
        return prob;
    }

    public double[] getProbabilities() {
        return probabilities;
    }

    public double[] getSortedProbabilities() {
        return sortedProbabilities;
    }

    public double getHighestProbability() {
        return sortedProbabilities[0];
    }

    public Speaker getMostProbableSpeaker() {
        return speakers[mostProbable];
    }
}
