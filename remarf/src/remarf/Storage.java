/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package remarf;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import marf.FeatureExtraction.LPC.LPC;

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

            int vectorSize = LPC.DEFAULT_POLES;

            String query = "SELECT count(*) FROM speakers";
            ResultSet rs = s.executeQuery(query);
            rs.next();

            int numSpeaker = rs.getInt(1) + 1;

            s.removeOpenResultSet(rs);

            query = "SELECT s.id, s.name, count(fs.index) c "
                    + "FROM features_stats fs "
                    + "JOIN speakers s ON s.id = fs.spk_id "
                    + "GROUP BY fs.spk_id "
                    + "ORDER BY fs.spk_id";
            rs = s.executeQuery(query);

            this.speakers = new Speaker[numSpeaker];
            this.speakers[0] = this.getDefaultSpeaker();

            int poles = 0;

            while (rs.next()) {
                this.speakers[rs.getRow()] = new Speaker();
                this.speakers[rs.getRow()].setId(rs.getInt("id"));
                this.speakers[rs.getRow()].setName(rs.getString("name"));
                poles = Math.max(poles, rs.getInt("c"));
            }

            s.removeOpenResultSet(rs);

            query = "SELECT fs.spk_id, fs.index, fs.mean, fs.variance FROM features_stats fs ORDER BY fs.spk_id, fs.index";
            rs = s.executeQuery(query);

            double[] mean = new double[poles];
            double[] variance = new double[poles];
            int n = 1;
            while (rs.next()) {
                int index = rs.getInt("index");
                mean[index] = trimDouble(rs.getDouble("mean"));
                variance[index] = trimDouble(rs.getDouble("variance"));
                if (index >= poles - 1) {
                    this.speakers[n].setMeanVector(mean.clone());
                    this.speakers[n].setVarianceVector(variance.clone());
                    n++;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }


    }

    private double trimDouble(double d) {
        double n = 10000000000.0;
        return Math.round(d * n) / n;
    }

    public int saveSpeaker(Speaker _speaker) {
        int id = 0;
        try {
            Statement s = (Statement) connection.createStatement();
            String query = "INSERT INTO speakers "
                    + "(name) "
                    + "VALUES"
                    + "('" + _speaker.getName() + "')";
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

    private Speaker getDefaultSpeaker() {
        Speaker noSpeaker = new Speaker();
        noSpeaker.setId(0);
        noSpeaker.setName("?");
        noSpeaker.setDistance(Double.MAX_VALUE);
        noSpeaker.setProbability(0);
        return noSpeaker;
    }
}
