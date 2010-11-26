/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ucungsr;

/**
 * @author mauricio
 */
public final class Classifier {

    private int mostProbable = 0;
    private Speaker[] speakers = null;
    private Speaker speaker = null;
    private double likehood;
    private double treshold;
    private double MIN_PROB = 0.5;
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
        mostProbable = 0;
        double prob = Double.MIN_VALUE;

        likehood = 0.0;
        double[] x = speaker.getMeanVector();

        for (int i = 1; i < speakers.length; i++) {

            double[] u = speakers[i].getMeanVector();
            double[] v = speakers[i].getVarianceVector();

            double[] calc = new double[3];
            calc = calculate(x, u, v);
            double d = calc[0];
            double p1 = calc[1];
            double p2 = calc[2];
            speakers[i].setProbability(p1);
            speakers[i].setDistance(d);
            likehood += p2;

//            java.util.Arrays.sort(u, i, i);
//            System.out.println(speakers[i].getName() + " p: " + tmpProb);

            if (p2 > prob && p1 > MIN_PROB) {
                prob = p2;
                mostProbable = i;
            }
        }


    }

    public boolean inSet() {
        return (mostProbable > 0 && likehood > treshold);
    }

    /**
     * @param x vector de características del sujeto que se analiza
     * @param u vector de media de los coeficientes LPC de una voz en la base.
     * @param v vector de varianza de los coeficientes LPC de una voz en la base.
     * @return result[0] distancia de Mahalanonbis;
     * @return result[1] double Math.exp(-distance / 2);
     * @return result[2] double probabilidad de pertenecer al Modelo;
     */
    private double[] calculate(double[] x, double[] u, double[] v) {
        double[] result = new double[3];
        int D = x.length;
        if (u.length == D && v.length == D) {
            double detSigma = 1.0;
            result[0] = 0.0;
            for (int i = 1; i < D; i++) { // No tomo desde i=0 porque los coeficientes LPC son cero... (arreglar)
                result[0] += mahDistance(x[i], u[i], v[i]);
                detSigma *= v[i];
            }
            //probabilidad de pertenecer al modelo dado por (u,v), considerando únicamente este modelo:
            result[1] = Math.exp(-result[0] / 2);
            result[2] = result[1] / Math.sqrt(Math.pow(2 * Math.PI, D - 1) * detSigma);
        }
        return result;
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

    /*
     * cmd = Diagonal de la matriz de covarianza
     */
    private double discriminant2(double[] x, double[] u, double[] cmd, double pwi) {
        int D = x.length;

        double term1 = 0.0;
        double term2 = -(D - 1) * Math.log(2 * Math.PI) / 2;
        double term3 = 1.0;
        double term4 = Math.log(pwi);

        for (int i = 1; i < D; i++) {
            term1 += Math.pow(x[i] - u[i], 2) / cmd[i];
            term3 *= cmd[i];
        }
        term1 = -term1 / 2;
        term3 = -Math.log(term3) / 2;

//        System.out.println(Double.toString(term1 + term2 + term3 + term4));
        return term1 + term2 + term3 + term4;
    }

    private double discriminant(double[] x, double[] u, double[] cmd, double pwi) {
        int D = x.length;

        double term1 = 0.0;
        double term2 = 0.0;
        double wi0term1 = 0.0;
        double wi0term2 = 1.0;

        for (int i = 1; i < D; i++) {
            term1 += x[i] * x[i] / cmd[i];
            term2 += u[i] * x[i] / cmd[i];
            wi0term1 += u[i] * u[i] / cmd[i];
            wi0term2 *= cmd[i];
        }

        double wi0 = -wi0term1 / 2 - Math.log(wi0term2) / 2 + Math.log(pwi);

        return term1 + term2 + wi0;
    }

    public int getModel() {
        mostProbable = 0;
        double maxDiscriminant = Double.MIN_VALUE;
        double pwi = 1.0 / (double) speakers.length;

        for (int i = 1; i < speakers.length; i++) {
            double[] x = speaker.getMeanVector();
            double[] u = speakers[i].getMeanVector();
            double[] cmd = speakers[i].getVarianceVector();
            double disc = discriminant(x, u, cmd, pwi);
            speakers[i].setProbability(disc);

//            System.out.println(speakers[i].getName()+" "+Double.toString(disc));

            if (disc > maxDiscriminant) {
                maxDiscriminant = disc;
                mostProbable = i;
                //Esto es para que inSet devuelva verdadero:
                likehood = treshold + 1;
            }
        }
        return mostProbable;
    }
}
