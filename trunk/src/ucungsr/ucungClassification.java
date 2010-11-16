/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ucungsr;

import marf.Classification.ClassificationException;
import marf.Classification.IClassification;
import marf.FeatureExtraction.IFeatureExtraction;
import marf.Storage.Result;
import marf.Storage.ResultSet;


/*
 * @author mauricio
 */
public class ucungClassification
        implements IClassification {

    protected IFeatureExtraction oFeatureExtraction = null;

    public boolean classify() throws ClassificationException {
        System.out.println("Classifying...");
        return true;
    }

    public boolean train() throws ClassificationException {
        throw new UnsupportedOperationException("Not supported yet 1.");
    }

    public Result getResult() {
        throw new UnsupportedOperationException("Not supported yet 2.");
    }

    public ResultSet getResultSet() {
        throw new UnsupportedOperationException("Not supported yet 3.");
    }

    public IFeatureExtraction getFeatureExtraction() {
        throw new UnsupportedOperationException("Not supported yet 4.");
    }

    public void setFeatureExtraction(IFeatureExtraction poFeatureExtraction) {
        this.oFeatureExtraction = poFeatureExtraction;
    }
}
