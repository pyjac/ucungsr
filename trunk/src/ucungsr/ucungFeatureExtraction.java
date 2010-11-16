/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ucungsr;

import marf.FeatureExtraction.FeatureExtractionException;
import marf.FeatureExtraction.IFeatureExtraction;
import marf.Preprocessing.IPreprocessing;

/**
 *
 * @author mauricio
 */
public class ucungFeatureExtraction implements IFeatureExtraction {

    protected IPreprocessing oPreprocessing = null;

    public boolean extractFeatures() throws FeatureExtractionException {
        System.out.println("Feature Extraction...");
        return true;
    }

    public double[] getFeaturesArray() {
        throw new UnsupportedOperationException("Not supported yet 1.");
    }

    public double[] getFeaturesVaianceArray() {
        throw new UnsupportedOperationException("Not supported yet 2.");
    }

    public IPreprocessing getPreprocessing() {
        throw new UnsupportedOperationException("Not supported yet 3.");
    }

    public void setPreprocessing(IPreprocessing poPreprocessing) {
        this.oPreprocessing = poPreprocessing;
    }
}
