/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ucungsr;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import marf.Classification.ClassificationException;
import marf.Classification.IClassification;
import marf.FeatureExtraction.IFeatureExtraction;
import marf.MARF;
import marf.Storage.Cluster;
import marf.Storage.Result;
import marf.Storage.ResultSet;
import marf.Storage.StorageException;
import marf.Storage.TrainingSet;


/*
 * @author mauricio
 */
public class ucungClassification
        implements IClassification {

    protected IFeatureExtraction oFeatureExtraction = null;
    protected ResultSet oResultSet = new ResultSet();
    protected TrainingSet oTrainingSet = null;
    int DUMP_GZIP_BINARY = 0;
    protected transient int iCurrentDumpMode = DUMP_GZIP_BINARY;

    public boolean classify() throws ClassificationException {
        try {
            System.out.println("Classifying...");
            // Features of the incoming sample
            double[] adIncomingFeatures = oFeatureExtraction.getFeaturesArray();
            // Restore training model from the disk
            restore();
            // Features in the training set
            Vector oTrainingSamples = this.oTrainingSet.getClusters();
            // Our minimum distance.
            double dMinDistance = Double.MAX_VALUE;
            /*
             * Run through the stored training samples set (mean vetors)
             * and determine the two closest subjects to the incoming features sample
             */
            for (int i = 0; i < oTrainingSamples.size(); i++) {
                Cluster oTrainingSample = (Cluster) oTrainingSamples.get(i);
                double[] adMeanVector = oTrainingSample.getMeanVector();
                // Sanity check: stored mean vector must never be null
                if (adMeanVector == null) {
                    throw new ClassificationException("Distance.classify() - Stored mean vector is null for subject (" + oTrainingSample.getSubjectID() + ", preprocessing method: " + this.oTrainingSet.getPreprocessingMethod() + ", feature extraction methods: " + this.oTrainingSet.getFeatureExtractionMethod());
                }
                if (adMeanVector.length != adIncomingFeatures.length) {
                    throw new ClassificationException("Distance.classify() - Mean vector length (" + adMeanVector.length + ") is not same as of incoming feature vector (" + adIncomingFeatures.length + ")");
                }
                double dCurrentDistance = distance(adMeanVector, adIncomingFeatures);
                if (dCurrentDistance < dMinDistance) {
                    dMinDistance = dCurrentDistance;
                } // XXX: Move to StatsCollector
                this.oResultSet.addResult(oTrainingSample.getSubjectID(), dCurrentDistance);
            }
            return true;
        } catch (StorageException ex) {
            Logger.getLogger(ucungClassification.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean train() throws ClassificationException {
        throw new UnsupportedOperationException("Not supported yet 1.");
    }

    public Result getResult() {
        return this.oResultSet.getMinimumResult();
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

    private double distance(double[] adMeanVector, double[] adIncomingFeatures) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Generic implementation of restore() for TrainingSet.
     * @since 0.2.0
     * @throws StorageException if there is a problem loading the training set from disk
     */
    public void restore()
            throws StorageException {
        loadTrainingSet();
    }

    /**
     * Loads TrainingSet from a file. Called by <code>restore()</code>.
     * @since 0.2.0
     * @throws StorageException if there is a problem loading the training set from disk
     */
    private void loadTrainingSet()
            throws StorageException {
        try {
            if (this.oTrainingSet == null) {
                this.oTrainingSet = new TrainingSet();
                this.oTrainingSet.setDumpMode(this.iCurrentDumpMode);
                this.oTrainingSet.setFilename(getTrainingSetFilename());
                this.oTrainingSet.restore();
            }


        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    protected String getTrainingSetFilename()
	{
		return
			// Fully-qualified class name
			this.oTrainingSet.getClass().getName() + "." +

			// Global cluster: <PR>.<FE>.<FVS>
			// For the same FE method we may have different feature vector sizes
			MARF.getPreprocessingMethod() + "." +
			MARF.getFeatureExtractionMethod() + "." +
			this.oFeatureExtraction.getFeaturesArray().length + "." +
                        this.oFeatureExtraction.getFeaturesVaianceArray().length + "." +
                        
			// Extension depending on the dump type
			"gzbin";
	}
}
