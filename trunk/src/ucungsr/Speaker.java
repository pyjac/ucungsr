/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ucungsr;

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

    public Speaker() {
    }

    @Override
    public String toString() {
        return "Speaker{" + "meanVector=" + meanVector + "varianceVector=" + varianceVector + "asociatedPhones=" + asociatedPhones + "asociatedWaveFiles=" + asociatedWaveFiles + "name=" + name + "id=" + id + '}';
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setMeanVector(double[] meanVector) {
        this.meanVector = meanVector;
    }

    public void setVarianceVector(double[] varianceVector) {
        this.varianceVector = varianceVector;
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
