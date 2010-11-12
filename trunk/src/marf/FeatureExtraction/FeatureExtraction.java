package marf.FeatureExtraction;

import marf.Preprocessing.IPreprocessing;
import marf.Storage.StorageManager;


/**
 * <p>Generic Feature Extraction Module.
 * Every feature extraction module must extend this class; if it cannot
 * then they must implement the IFeatureExtraction interface.
 * </p>
 *
 * <p>$Id: FeatureExtraction.java,v 1.33 2006/01/21 02:35:31 mokhov Exp $</p>
 *
 * @author Serguei Mokhov
 * @version $Revision: 1.33 $
 * @since 0.0.1
 */
public abstract class FeatureExtraction
extends StorageManager
implements IFeatureExtraction
{
	/**
	 * Internal reference to the Preprocessing module.
	 */
	protected IPreprocessing oPreprocessing = null;

	/**
	 * An array of features extracted (coefficiencies and/or amplitude values).
	 */
	protected double[] adFeatures = null;

	/**
         * @author Mauricio van der Maesen
	 * An array of mean values of the extracted coeficients (for LPC).
	 */
        protected double[] coefMean = null;


	/**
         * @author Mauricio van der Maesen
	 * An array of standard desviation values of the extracted coeficients (for LPC).
	 */
        protected double[] coefStdDesv = null;


	/**
	 * For serialization versioning.
	 * When adding new members or make other structural
	 * changes regenerate this number with the
	 * <code>serialver</code> tool that comes with JDK.
	 * @since 0.3.0.5
	 */
	private static final long serialVersionUID = 7608607169147548576L;

	/**
	 * Main FeatureExtraction constructor.
	 * @param poPreprocessing preprocessing object ref.
	 */
	protected FeatureExtraction(IPreprocessing poPreprocessing)
	{
		this.oPreprocessing = poPreprocessing;
		this.iCurrentDumpMode = DUMP_GZIP_BINARY;
		this.oObjectToSerialize = this.adFeatures;
	}

	/**
	 * Allows retrieval of the internal feature vector.
	 * @return array of features (<code>double</code> values)
	 */
	public final double[] getFeaturesArray()
	{
		return this.adFeatures;
	}

        /**
	 * Allows retrieval of the mean values of LPC coeficients.
	 * @return array of mean values (<code>double</code> values)
	 */
        public final double[] getFeaturesMeanArray()
	{
		return this.coefMean;
	}


        /**
	 * Allows retrieval of the standard desviation values of LPC coeficients.
	 * @return array of mean values (<code>double</code> values)
	 */
        public final double[] getFeaturesStdDesvArray()
	{
		return this.coefStdDesv;
	}


	/**
	 * Retrieves inner preprocessing reference.
	 * @return the preprocessing reference
	 * @since 0.3.0.4
	 */
	public IPreprocessing getPreprocessing()
	{
		return this.oPreprocessing;
	}

	/**
	 * Allows setting the source preprocessing module.
	 * @param poPreprocessing the preprocessing object to set
	 * @since 0.3.0.4
	 */
	public void setPreprocessing(IPreprocessing poPreprocessing)
	{
		this.oPreprocessing = poPreprocessing;
	}

	/**
	 * Implementaion of back-synchronization of loaded object.
	 * @since 0.3.0.3
	 */
	public void backSynchronizeObject()
	{
		this.adFeatures = (double[])this.oObjectToSerialize;
	}

	/**
	 * Implementes Cloneable interface for the FeatureExtraction object.
	 * The contained Preprocessing isn't cloned at this point,
	 * and is just assigned to the clone.
	 * @see java.lang.Object#clone()
	 * @since 0.3.0.5
	 */
	public Object clone()
	{
		FeatureExtraction oClone = (FeatureExtraction)super.clone();
		oClone.adFeatures = (double[])this.adFeatures.clone();
		oClone.oPreprocessing = this.oPreprocessing;
		return oClone;
	}

	/**
	 * Returns source code revision information.
	 * @return revision string
	 * @since 0.3.0.2
	 */
	public static String getMARFSourceCodeRevision()
	{
		return "$Revision: 1.33 $";
	}
}

// EOF
