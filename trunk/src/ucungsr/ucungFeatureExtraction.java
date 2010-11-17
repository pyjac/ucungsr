/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ucungsr;

import marf.FeatureExtraction.FeatureExtractionException;
import marf.FeatureExtraction.IFeatureExtraction;
import marf.Preprocessing.IPreprocessing;
import marf.math.Algorithms;

/**
 *
 * @author mauricio
 */
public class ucungFeatureExtraction implements IFeatureExtraction {

    private int iPoles = 10;
    private int iWindowLen = 128;
    protected IPreprocessing oPreprocessing = null;
    protected double[] adFeatures = null;
    protected double[] coefVariance = null;

    public boolean extractFeatures() throws FeatureExtractionException {
        System.out.println("Feature Extraction...");
        try {
            double[] adSample = this.oPreprocessing.getSample().getSampleArray();

            this.adFeatures = new double[this.iPoles];

            double[] adWindowed = new double[this.iWindowLen];
            double[] adLPCCoeffs = new double[this.iPoles];
            double[] adLPCError = new double[this.iPoles];

            // Number of windows
            int iWindowsNum = 1;

            int iHalfWindow = this.iWindowLen / 2;

            this.coefVariance = new double[this.iPoles];

            for (int i = 0; i < this.iPoles; i++) {
                this.adFeatures[i] = 0;
                this.coefVariance[i] = 0;
            }

            for (int iCount = iHalfWindow; (iCount + iHalfWindow) <= adSample.length; iCount += iHalfWindow) {
                // Window the input.
                for (int j = 0; j < this.iWindowLen; j++) {
                    adWindowed[j] = adSample[iCount - iHalfWindow + j];
                }

                Algorithms.Hamming.hamming(adWindowed);
                Algorithms.LPC.doLPC(adWindowed, adLPCCoeffs, adLPCError, this.iPoles);



                // Collect features
                double[] mean0 = new double[this.iPoles];
                int i = (int) Math.floor((iCount - iHalfWindow) / iHalfWindow);

                for (int j = 0; j < this.iPoles; j++) {
                    mean0[j] = this.adFeatures[j];
                    this.adFeatures[j] = (i * mean0[j] + adLPCCoeffs[j]) / (i + 1); // u_(N+1) = (N*u_N + x_(N+1))/N+1
                    this.coefVariance[j] = (i * this.coefVariance[j] + i * Math.pow((mean0[j] - this.adFeatures[j]), 2) + Math.pow((adLPCCoeffs[j] - this.adFeatures[j]), 2)) / (i + 1);

                }

                iWindowsNum++;
            }


            return (this.adFeatures.length > 0);
        } catch (Exception e) {
            throw new FeatureExtractionException(e);
        }
    }

    public double[] getFeaturesArray() {
        return this.adFeatures;
    }

    public double[] getFeaturesVaianceArray() {
        return this.coefVariance;
    }

    public IPreprocessing getPreprocessing() {
        return this.oPreprocessing;
    }

    public void setPreprocessing(IPreprocessing poPreprocessing) {
        this.oPreprocessing = poPreprocessing;
    }
}
