/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ucungsr;

import java.util.logging.Level;
import java.util.logging.Logger;
import marf.Classification.ClassificationException;
import marf.Classification.ClassificationFactory;
import marf.Classification.IClassification;
import marf.FeatureExtraction.FeatureExtractionException;
import marf.FeatureExtraction.FeatureExtractionFactory;
import marf.FeatureExtraction.IFeatureExtraction;
import marf.MARF;
import marf.Preprocessing.IPreprocessing;
import marf.Preprocessing.PreprocessingException;
import marf.Preprocessing.PreprocessingFactory;
import marf.Storage.ISampleLoader;
import marf.Storage.Sample;
import marf.Storage.SampleLoaderFactory;
import marf.Storage.StorageException;
import marf.util.InvalidSampleFormatException;
import marf.util.MARFException;

/**
 *
 * @author mauricio
 */
public class SR2 {

    private String sstrFileName = null;
    private static ISampleLoader soSampleLoader = null;
    private static IPreprocessing soPreprocessing = null;
    private static Sample soSample = null;
    private static IFeatureExtraction soFeatureExtraction = null;
    private static IClassification soClassification = null;

    SR2(String[] args) {
        sstrFileName = args[0];

        marfConfig();

        //Cargo archivo en MARF:
        loadFile();

        //Preprocesamiento:
        preprocess();

        //Extracción de características:
        featureExtraction();

        //Clasificación:
        classification();

    }

    private void loadFile() {
        MARF.setSampleFile(sstrFileName);
        try {
            soSampleLoader = SampleLoaderFactory.create(MARF.getSampleFormat());
            soSample = soSampleLoader.loadSample(sstrFileName);
        } catch (StorageException ex) {
            Logger.getLogger(SR2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidSampleFormatException ex) {
            Logger.getLogger(SR2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void marfConfig() {
        try {
            MARF.setPreprocessingPluginClass("ucungsr.ucungPreprocessing");
            MARF.setPreprocessingMethod(MARF.PREPROCESSING_PLUGIN);

            MARF.setFeatureExtractionPluginClass("ucungsr.ucungFeatureExtraction");
            MARF.setFeatureExtractionMethod(MARF.FEATURE_EXTRACTION_PLUGIN);

            MARF.setClassificationPluginClass("ucungsr.ucungClassification");
            MARF.setClassificationMethod(MARF.CLASSIFICATION_PLUGIN);

            MARF.setSampleFormat(MARF.WAV);
            MARF.setDumpSpectrogram(false);

            
        } catch (MARFException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }

    }

    private void preprocess() {
        try {
            soPreprocessing = PreprocessingFactory.create(MARF.getPreprocessingMethod(), soSample);
            boolean preprocess = soPreprocessing.preprocess();
        } catch (PreprocessingException ex) {
            Logger.getLogger(SR2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void featureExtraction() {
        try {
            soFeatureExtraction = FeatureExtractionFactory.create(MARF.getFeatureExtractionMethod(), soPreprocessing);
            boolean extractFeatures = soFeatureExtraction.extractFeatures();
        } catch (FeatureExtractionException ex) {
            Logger.getLogger(SR2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void classification() {
        try {
            soClassification = ClassificationFactory.create(MARF.getClassificationMethod(), soFeatureExtraction);
            boolean classify = soClassification.classify();
        } catch (ClassificationException ex) {
            Logger.getLogger(SR2.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
