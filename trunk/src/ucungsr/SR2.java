/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ucungsr;

import java.util.logging.Level;
import java.util.logging.Logger;
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

        setXSpeaker();

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
            if (args.length > 2) {
                speaker.setName(args[2]);
            }
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
            System.out.println(ex.getMessage());
        } catch (InvalidSampleFormatException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void marfConfig() {
        try {
//            MARF.setPreprocessingPluginClass("ucungsr.ucungPreprocessing");
//            MARF.setFeatureExtractionPluginClass("ucungsr.ucungFeatureExtraction");
//            MARF.setClassificationPluginClass("ucungsr.ucungClassification");
//            MARF.setPreprocessingMethod(MARF.PREPROCESSING_PLUGIN);
//            MARF.setFeatureExtractionMethod(MARF.FEATURE_EXTRACTION_PLUGIN);
//            MARF.setClassificationMethod(MARF.CLASSIFICATION_PLUGIN);

            MARF.setPreprocessingMethod(MARF.DUMMY);
            MARF.setFeatureExtractionMethod(MARF.LPC);
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


//            System.out.println("getFeaturesVaianceArray"+Double.toString(soFeatureExtraction.getFeaturesVaianceArray()[2]));

        } catch (FeatureExtractionException ex) {
        }
    }

    private synchronized void classification() {
        Classifier classifier = new Classifier(speaker, speakers);
        classifier.classify();
        
        int mpsi = classifier.getMostProbableSpeakerIndex();

        Speaker MPS = classifier.getMostProbableSpeaker();
        int id = MPS.getId();
        String name = MPS.getName();
        double prob = MPS.getProbability();

        String result = "";
        if(this.sstrFileName.contains(name)){
            result = "OK";
        }
        if (classifier.inSet()) {
            System.out.println(this.sstrFileName + " | ID: " + id + " | " + name + " | prob: " + Double.toString(prob) + " | " + result);
        } else {
            System.out.println(this.sstrFileName + " | No reconocido.");
            System.out.println("ID: " + id + " | " + name + " | prob: " + Double.toString(prob));

        }
    }

    private void saveFeatureExtraction() {
        int saveSpeaker = db.saveSpeaker(speaker);
    }

    private void loadDB() {
        db = new Storage();
        speakers = db.getSpeakers();
    }

    private void setXSpeaker() {
        speaker = new Speaker();
        speaker.setName("?");
    }
}
