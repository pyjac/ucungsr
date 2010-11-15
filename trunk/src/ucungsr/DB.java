/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ucungsr;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import marf.FeatureExtraction.ucung.ucungFeatureExtraction;

/**
 *
 * @author mauricio
 */
public class DB {
    private double[] coefMean = null;
    private double[] coefVariance = null;
    private Connection conexion;

    public DB() {
        try {
            conectar();
//            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void conectar() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
        }
        conexion = DriverManager.getConnection("jdbc:mysql://localhost/ng1", "root", "");
    }

    public int saveStatistics(int id) throws SQLException {
        Statement s = conexion.createStatement();
        if (id > -1) {
            for (int i = 0; i < ucungFeatureExtraction.DEFAULT_POLES; i++) {
                String query = "INSERT INTO speakers (id, nombre) "
                        + "VALUES (" + id + ", '') "
                        + "ON DUPLICATE KEY "
                        + "UPDATE nombre = ''";
                s.execute(query);

                query = "INSERT INTO spk_coef (mean, stdesv, spk_id, coef) VALUES ("
                        + Double.toString(coefMean[i]) + ", "
                        + Double.toString(coefVariance[i]) + ", "
                        + id + ", "
                        + i +") "
                        + "ON DUPLICATE KEY "
                        + "UPDATE mean = " + Double.toString(coefMean[i]) + ", "
                        + "stdesv = " + Double.toString(coefVariance[i]);

                s.execute(query);
            }
        } else {
            String query = "INSERT INTO speakers (nombre) VALUES (\"\")";

            System.out.println(query);

            s.execute(query);
            query = "SELECT LAST_INSERT_ID()";
            ResultSet rs = s.executeQuery(query);
            rs.next();
            id = rs.getInt(1);

            for (int i = 0; i < ucungFeatureExtraction.DEFAULT_POLES; i++) {
                query = "INSERT INTO spk_coef (spk_id, coef, mean, stdesv) "
                        + "VALUES (" + id + ", " + i + ", "
                        + Double.toString(coefMean[i]) + ", "
                        + Double.toString(coefVariance[i]) + ")";
                s.execute(query);
            }
        }
        return id;
    }

    public void loadStatistics(int id) throws SQLException {
        Statement s = conexion.createStatement();
        ResultSet rs = s.executeQuery("SELECT coef,mean,stdesv FROM spk_coef WHERE spk_id = " + id);

        coefMean = new double[ucungFeatureExtraction.DEFAULT_POLES];
        coefVariance = new double[ucungFeatureExtraction.DEFAULT_POLES];

        while (rs.next()) {
            int indice = rs.getInt(1);
            coefMean[indice] = rs.getDouble(2);
            coefVariance[indice] = rs.getDouble(3);
        }
    }

    public double[] getMeanValues(int id) {
        return coefMean;
    }

    public double[] getStdDesvValues(int id) {
        return coefVariance;
    }

    public void setMeanValues(double[] _coefMean) {
        coefMean = _coefMean;
    }

    public void setStdDesvValues(double[] _coefStdDesv) {
        coefVariance = _coefStdDesv;
    }
}
