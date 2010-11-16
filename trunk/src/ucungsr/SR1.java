 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ucungsr;

import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import marf.FeatureExtraction.LPC.LPC;
import marf.MARF;
import marf.util.MARFException;

/**
 *
 * @author mauricio
 */
public class SR1 {

    /**
     * @param args the command line arguments
     */
    SR1(String[] args) {

        marfConfig();

        if (args.length >= 2) {
            File fileOrDir = new File(args[1]);
            if (!fileOrDir.isDirectory()) {
                MARF.setSampleFile(args[1]);
            }
        } else {
            System.out.println("Usos correctos:");
            System.out.println("java -jar \"dist/identificadorLPC.jar\" -train archivo id");
            System.out.println("java -jar \"dist/identificadorLPC.jar\" -ident archivo");
        }


        if (args[0].equals("-train")) {
            train(Integer.valueOf(args[2]));
        }



        if (args[0].equals("-ident")) {
            ident();
            distancias(true);
        }

//            caracteristicas(true);

    }

    private static void marfConfig() {
        try {
//            MARF.setPreprocessingMethod(MARF.DUMMY); // Normalizaci√≥n
//            MARF.setFeatureExtractionMethod(MARF.LPC); // TODO: Agregar dato de media y varianza para evaluar resultado
//            MARF.setClassificationMethod(MARF.MAHALANOBIS_DISTANCE);

//            MARF.setPreprocessingPluginClass("ucungsr.ucungPreprocessing");
//            MARF.setPreprocessingMethod(MARF.PREPROCESSING_PLUGIN);
//            MARF.setPreprocessingMethod(MARF.UCUNG_PREPROCESSING);

//            MARF.setFeatureExtractionPluginClass("ucungsr.ucungFeatureExtraction");
//            MARF.setFeatureExtractionMethod(MARF.FEATURE_EXTRACTION_PLUGIN);
//            MARF.setFeatureExtractionMethod(MARF.UCUNG_FEATURE_EXTRACTION); // TODO: Agregar dato de media y varianza para evaluar resultado

//            MARF.setClassificationPluginClass("ucungsr.ucungClassification");
//            MARF.setClassificationMethod(MARF.CLASSIFICATION_PLUGIN);
//            MARF.setClassificationMethod(MARF.UCUNG_CLASSIFICATION);

            MARF.setPreprocessingMethod(MARF.DUMMY); // Normalizaci√≥n
            MARF.setFeatureExtractionMethod(MARF.LPC); // TODO: Agregar dato de media y varianza para evaluar resultado
            MARF.setClassificationMethod(MARF.MAHALANOBIS_DISTANCE);

            
            MARF.setDumpSpectrogram(false);
            MARF.setSampleFormat(MARF.WAV);

        } catch (MARFException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }

    }

    private static void train(int id) {
        System.out.println("* TRAIN *");
        try {
            MARF.setCurrentSubject(id);
            MARF.train();
//            caracteristicas(true);
//            getVariance(true);
            try {
               
                DB db = new DB();

                db.setMeanValues(caracteristicas(false));
                db.setStdDesvValues(getVariance(false));

                int lastId = db.saveStatistics(id);
                System.out.println("LAST MYSQL ID:\t" + lastId);

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

        } catch (MARFException ex) {
        }

    }

    private static int ident() {
        System.out.println("* RECOGNIZE *");
        int id = -1;
        try {
            MARF.recognize();
            id = MARF.queryResultID();
            System.out.println("ID:\t" + id);

            double prob = probabilidad(id);
            System.out.println("Probabilidad:\t " + Double.toString(prob));
        } catch (MARFException ex) {
        }
        return id;
    }

    private static double probabilidad(int id) {
        double prob = 0;
        try {
            DB db = new DB();
            db.loadStatistics(id);
            double[] mean = db.getMeanValues(id);
            double[] stdesv = db.getStdDesvValues(id);
            double[] featuesVector = caracteristicas(false);
            double detSigma = 1;
            double d2 = 0;
            for (int i = 1; i < LPC.DEFAULT_POLES; i++) {
                d2 += Math.pow(featuesVector[i] - mean[i], 2) / stdesv[i];
                detSigma *= stdesv[i];
            }


            double b = Math.pow(2 * Math.PI, LPC.DEFAULT_POLES - 1) * detSigma;
            prob = Math.sqrt(Math.exp(-d2) / b);

        } catch (SQLException ex) {
            Logger.getLogger(SR1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return prob;
    }

    private static double[] distancias(boolean print) {
        double distancias[] = new double[MARF.getResultSet().getResultSetSorted().length];
        if (print) {
            System.out.println("ID\tDistancia\tProb");
            for (int i = 0; i < MARF.getResultSet().getResultSetSorted().length; i++) {
                int id = MARF.getResultSet().getResultSetSorted()[i].getID();
                double outcome = MARF.getResultSet().getResultSetSorted()[i].getOutcome();
                distancias[id - 1] = outcome;
                System.out.println(id + "\t" + Double.toString(distancias[id - 1]).replaceAll("\\.", ",") + "\t" + Double.toString(probabilidad(id)).replaceAll("\\.", ","));
            }
        }
        return distancias;
    }

    public static double[] caracteristicas(boolean print) {
        double[] FeaturesArray = MARF.getFeatureExtraction().getFeaturesArray();
        if (print) {
            String vector = new String();
            for (int i = 0; i < FeaturesArray.length; i++) {
                vector += Double.toString(FeaturesArray[i]).replaceAll("\\.", ",") + "\t";
            }
            System.out.println("* Features Vector *");
            System.out.println(vector);
        }
        return FeaturesArray;
    }

    public static double[] getVariance(boolean print) {
        double[] FeaturesVarianceArray = MARF.getFeatureExtraction().getFeaturesVaianceArray();
        if (print) {
            String vector = new String();
            for (int i = 0; i < FeaturesVarianceArray.length; i++) {
                vector += Double.toString(FeaturesVarianceArray[i]).replaceAll("\\.", ",") + "\t";
            }
            System.out.println("* Variance Vector *");
            System.out.println(vector);
        }
        return FeaturesVarianceArray;
    }
}
