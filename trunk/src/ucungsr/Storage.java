/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ucungsr;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mauricio
 */
public class Storage {

    private Speaker[] speakers = null;
    private Connection connection;
    private String DB_NAME = "ucungsr";
    private String DB_USER = "root";
    private String DB_PASSWORD = "";

    public Storage() {
        try {
            connect();
            load();
        } catch (SQLException ex) {
        }
    }

    private void connect() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
        }
        connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/" + DB_NAME, DB_USER, DB_PASSWORD);
    }

    public Speaker[] getSpeakers() {
        return speakers;
    }

    private void load() {
        try {
            Statement s = (Statement) connection.createStatement();

            int vectorSize = 10;
            double[] mean = new double[vectorSize];
            double[] variance = new double[vectorSize];

            String query = "SELECT count(*) " + "FROM speakers";
            ResultSet rs = s.executeQuery(query);
            rs.next();

            int numSpeaker = rs.getInt(1);
            this.speakers = new Speaker[numSpeaker];
            s.removeOpenResultSet(rs);
            
            query = "SELECT s.id, s.name, fs.index, fs.mean, fs.variance " + "FROM features_stats fs " + "JOIN speakers s ON s.id = fs.spk_id " + "ORDER BY fs.spk_id, fs.index";
            rs = s.executeQuery(query);
            
            int id = -1;
            int n = 0;
            Speaker spk = new Speaker();
            
            while (rs.next()) {
                if (rs.getInt(1) != id) {
                    if (rs.getRow() > 1) {
                        spk.setMeanVector(mean);
                        spk.setVarianceVector(variance);
                        speakers[n++] = spk;
                    }
                    id = rs.getInt(1);
                    spk = new Speaker();
                }
                int index = rs.getInt(3);


                mean[index] = rs.getDouble(4);
                variance[index] = rs.getDouble(5);
                spk.setId(index);
                spk.setName(rs.getString(2));
            }
            if (id != -1) {
                spk.setMeanVector(mean);
                spk.setVarianceVector(variance);
                speakers[n] = spk;
            }


        } catch (SQLException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int saveSpeaker(Speaker _speaker) {
        int id = -1;
        try {
            Statement s = (Statement) connection.createStatement();
            String query = "INSERT INTO speakers "
                    + "(name) "
                    + "VALUES"
                    + "('')";
            s.execute(query);
            query = "SELECT LAST_INSERT_ID()";
            ResultSet rs = s.executeQuery(query);
            rs.next();
            id = rs.getInt(1);
            s.removeOpenResultSet(rs);

            s.clearBatch();

            for (int i = 0; i < _speaker.getMeanVector().length; i++) {

                query = "INSERT INTO features_stats "
                        + "(features_stats.spk_id, features_stats.index, features_stats.mean, features_stats.variance) "
                        + "VALUES (" + id + ", " + i + ", "
                        + Double.toString(_speaker.getMeanVector()[i]) + ", "
                        + Double.toString(_speaker.getVarianceVector()[i]) + ");";

                s.addBatch(query);

            }
            s.executeBatch();


        } catch (SQLException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
}
