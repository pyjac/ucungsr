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

            String query = "SELECT count(*) " + "FROM speakers";
            ResultSet rs = s.executeQuery(query);
            rs.next();

            int numSpeaker = rs.getInt(1);

            s.removeOpenResultSet(rs);

            query = "SELECT s.id, s.name, count(fs.index) c "
                    + "FROM features_stats fs "
                    + "JOIN speakers s ON s.id = fs.spk_id "
                    + "GROUP BY fs.spk_id "
                    + "ORDER BY fs.spk_id";
            rs = s.executeQuery(query);


            this.speakers = new Speaker[numSpeaker];

            while (rs.next()) {
                speakers[rs.getRow() - 1] = new Speaker();
                speakers[rs.getRow() - 1].setId(rs.getInt("id"));
                speakers[rs.getRow() - 1].setName(rs.getString("name"));
                speakers[rs.getRow() - 1].setMeanVector(new double[rs.getInt("c")]);
                speakers[rs.getRow() - 1].setVarianceVector(new double[rs.getInt("c")]);
            }

            s.removeOpenResultSet(rs);

            query = "SELECT * FROM features_stats ORDER BY spk_id";
            rs = s.executeQuery(query);


            double[] mean = null;
            double[] variance = null;
            int id = -1;
            int n = 0;
            while (rs.next()) {
                if (rs.getInt("spk_id") != id) {
                    id = rs.getInt("spk_id");
                    mean = speakers[n].getMeanVector();
                    variance = speakers[n].getVarianceVector();
                    n++;
                }
                mean[rs.getInt("index")] = rs.getDouble("mean");
                variance[rs.getInt("index")] = rs.getDouble("variance");
                speakers[n - 1].setMeanVector(mean);
                speakers[n - 1].setVarianceVector(variance);
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
