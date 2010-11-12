 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ucungsr;

import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            distancias();
        }

//            caracteristicas();

    }

    private static void marfConfig() {
        try {
            MARF.setPreprocessingMethod(MARF.DUMMY); // Normalización
            MARF.setFeatureExtractionMethod(MARF.LPC); // TODO: Agregar dato de media y varianza para evaluar resultado
            MARF.setClassificationMethod(MARF.MAHALANOBIS_DISTANCE);
            MARF.setDumpSpectrogram(true);
            MARF.setSampleFormat(MARF.WAV);

        } catch (MARFException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);



        }

    }

    private static void train(int id) {

        System.out.println("* TRAIN *");

        MARF.setCurrentSubject(id);
        try {
            MARF.train();

            try {
                double[] statistics = new double[2];
                statistics[0] = Math.random();
                statistics[1] = Math.random();

                DB db = new DB();
                int lastId = db.saveStatistics(statistics, -1);
                System.out.println("LAST MYSQL ID:\t" + lastId);


                statistics = db.getStatistics(lastId);
                System.out.println("Media:\t" + Double.toString(statistics[0]) + "\nVarianza:\t" + Double.toString(statistics[1]));

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

            DB db = new DB();
            double[] statistics = db.getStatistics(id);
            System.out.println("Media:\t" + Double.toString(statistics[0]) + "\nVarianza:\t" + Double.toString(statistics[1]));

            System.out.println("ID:\t" + id);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (MARFException ex) {
        }
        return id;
    }

    private static double[] distancias() {
        double distancias[] = new double[MARF.getResultSet().getResultSetSorted().length];
        for (int i = 0; i < MARF.getResultSet().getResultSetSorted().length; i++) {
            int id = MARF.getResultSet().getResultSetSorted()[i].getID();
            double outcome = MARF.getResultSet().getResultSetSorted()[i].getOutcome();
            distancias[id - 1] = outcome;
            System.out.println(id + "\t" + distancias[id - 1]);
        }
        return distancias;
    }

    public static double[] caracteristicas() {
        double[] FeaturesArray = MARF.getFeatureExtraction().getFeaturesArray();
//        String vector = new String();
//        for (int i = 0; i < FeaturesArray.length; i++) {
//            vector += FeaturesArray[i] + "\t";
//        }
//        System.out.println(vector);
        return FeaturesArray;
    }
}
