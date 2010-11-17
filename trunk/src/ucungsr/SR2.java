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
    private Storage db = null;
    private Speaker[] speakers = null;
    private Speaker speaker = null;

    SR2(String[] args) {
        sstrFileName = args[0];

        //levanto base de datos para tenerla en menoria:
        loadDB();


        //configuro MARF:
        marfConfig();

        //Cargo archivo de audio en MARF:
        loadFile();

        //Preprocesamiento:
        preprocess();

        //Extracción de características:
        featureExtraction();

        if (args.length > 1 && args[1].equals("-insert")) {
            //Guardo en Base de datos:
            saveFeatureExtraction();
        } else {
            //Clasificación:
            classification();
        }
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
            MARF.setPreprocessingMethod(MARF.DUMMY);

            MARF.setFeatureExtractionPluginClass("ucungsr.ucungFeatureExtraction");
            MARF.setFeatureExtractionMethod(MARF.LPC);

            MARF.setClassificationPluginClass("ucungsr.ucungClassification");
            MARF.setClassificationMethod(MARF.MAHALANOBIS_DISTANCE);

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
        }
    }

    private void featureExtraction() {
        try {
            soFeatureExtraction = FeatureExtractionFactory.create(MARF.getFeatureExtractionMethod(), soPreprocessing);
            boolean extractFeatures = soFeatureExtraction.extractFeatures();

            speaker = new Speaker();
            speaker.setMeanVector(soFeatureExtraction.getFeaturesArray());
            speaker.setVarianceVector(soFeatureExtraction.getFeaturesVaianceArray());

        } catch (FeatureExtractionException ex) {
        }
    }

    private void classification() {

        Classifier classifier = new Classifier(speaker, speakers);
        int id = classifier.getMostProbableId();
        System.out.println("Speaker ID:\t"+id);

        double prob = classifier.getProbability();
        System.out.println("Probability:\t"+Double.toString(prob));

        try {
            soClassification = ClassificationFactory.create(MARF.getClassificationMethod(), soFeatureExtraction);
            boolean classify = soClassification.classify();


            System.out.println(soClassification.getResult().getDescription());
            soClassification.getResultSet().size();
//            System.out.println(Double.toString(soClassification.getResult().getOutcome()));
//            Result[] rs = soClassification.getResultSet().getResultSetSorted();
//            System.out.println(rs[1].getOutcome());

//            System.out.println(soClassification.getResult().getID());
//            System.out.println(soClassification.getResult().getOutcome());

        } catch (ClassificationException ex) {
        }
    }

    private void saveFeatureExtraction() {
        int saveSpeaker = db.saveSpeaker(speaker);
        
    }

    private void loadDB() {
        db = new Storage();
        speakers = db.getSpeakers();
    }
}
