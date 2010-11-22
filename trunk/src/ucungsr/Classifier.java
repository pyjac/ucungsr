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

    private int mostProbable;
    private Speaker[] speakers = null;
    private Speaker speaker = null;
    private double likehood;
    private double treshold;
    private double DEFAULT_THRESHOLD = 0.1;

    public Classifier(Speaker _speaker, Speaker[] _speakers) {
        speaker = _speaker;
        speakers = _speakers;
        treshold = DEFAULT_THRESHOLD;
    }

    public void setTreshold(double treshold) {
        this.treshold = treshold;
    }

    public void classify() {
        mostProbable = -1;
        double prob = Double.MIN_VALUE;
        double tmpProb;

        likehood = 0.0;
        double[] x = speaker.getMeanVector();
        double[] u = new double[x.length];
        double[] v = new double[x.length];
        for (int i = 0; i < speakers.length; i++) {

            u = speakers[i].getMeanVector();
            v = speakers[i].getVarianceVector();

            tmpProb = px(x, u, v);
            speakers[i].setProbability(tmpProb);
            likehood += tmpProb;

            System.out.println(speakers[i].getName() + " p: " + tmpProb);

            if (tmpProb > prob) {
                prob = tmpProb;
                mostProbable = i;
            }
        }
    }

    public boolean inSet() {
        return (likehood > treshold);
    }

    private double px(double[] x, double[] u, double[] v) {
        int D = x.length;
        if (u.length == D && v.length == D) {
            double detSigma = 1.0;
            double distance = 0.0;
            for (int i = 1; i < D; i++) { // No tomo desde i=0 porque los coeficientes LPC son cero... (arreglar)
                distance += mahDistance(x[i], u[i], v[i]);
                detSigma *= v[i];
            }
            return Math.exp(-distance / 2) / Math.sqrt(Math.pow(2 * Math.PI, D - 1) * detSigma);
        } else {
            return 0.0;
        }
    }

    private double mahDistance(double x, double u, double v) {
        return Math.pow(x - u, 2) / v;
    }


    public Speaker getMostProbableSpeaker() {
        return speakers[mostProbable];
    }

    public int getMostProbableSpeakerIndex() {
        return mostProbable;
    }
}
