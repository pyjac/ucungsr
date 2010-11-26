/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package remarf;

/**
 *
 * @author mauricio
 */
public class Speaker {

    private double[] meanVector = null;
    private double[] varianceVector = null;
    private String[] asociatedPhones = null;
    private String[] asociatedWaveFiles = null;
    private String name;
    private int id;
    private double distance;
    private double probability;

    public double getDistance() {
        return distance;
    }

    public double getProbability() {
        return probability;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public Speaker() {
    }

    @Override
    public String toString() {
        String mean = "meanVector\t";
        for (int i = 0; i < meanVector.length; i++) {
            mean += Double.toString(meanVector[i]) + " ";
        }
        String variance = "varianceVector\t";
        for (int i = 0; i < varianceVector.length; i++) {
            variance += Double.toString(varianceVector[i]) + " ";
        }

        return "ID\t" + id + "\tName\t" + name + "\n" + mean + "\n" + variance;
    }

    public double[] getMeanVector() {
        return meanVector;
    }

    public double[] getVarianceVector() {
        return varianceVector;
    }

    public String[] getAsociatedPhones() {
        return asociatedPhones;
    }

    public String[] getAsociatedWaveFiles() {
        return asociatedWaveFiles;
    }

    public synchronized int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setMeanVector(double[] meanVector) {
        this.meanVector = meanVector.clone();
    }

    public void setVarianceVector(double[] varianceVector) {
        this.varianceVector = varianceVector.clone();
    }

    public void setAsociatedPhones(String[] asociatedPhones) {
        this.asociatedPhones = asociatedPhones;
    }

    public void setAsociatedWaveFiles(String[] asociatedWaveFiles) {
        this.asociatedWaveFiles = asociatedWaveFiles;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
