/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ucungsr;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mauricio
 */
public class DB {

    private Connection conexion;

    public DB() {
        try {
            conectar();
//            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int saveStatistics(double[] statistics, int id) throws SQLException {
        Statement s = conexion.createStatement();
        if (id > -1) {
            String query = "UPDATE speakers SET mean = " + Double.toString(statistics[0]) + ", variance = " + Double.toString(statistics[1]) + ")";
            s.execute(query);
        } else {
            String query = "INSERT INTO speakers (mean,variance) VALUES (" + Double.toString(statistics[0]) + "," + Double.toString(statistics[1]) + ")";
            s.execute(query);
            query = "SELECT LAST_INSERT_ID()";
            ResultSet rs = s.executeQuery(query);
            rs.next();
            id = rs.getInt(1);
        }
        return id;
    }

    public double[] getStatistics(int id) throws SQLException {
        Statement s = conexion.createStatement();
        ResultSet rs = s.executeQuery("SELECT mean,variance FROM speakers WHERE id = " + id);
        double[] statistics = new double[2];
        while (rs.next()) {
            statistics[0] = rs.getDouble(1);
            statistics[1] = rs.getDouble(2);
        }
        return statistics;
    }

    private void conectar() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
        }
        conexion = DriverManager.getConnection("jdbc:mysql://localhost/ng1", "root", "");
    }
}
