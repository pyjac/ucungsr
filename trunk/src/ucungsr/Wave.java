/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ucungsr;

/**
 *
 * @author mauricio
 */
public class Wave {
    private int id;
    private String path;
    private Speaker[] speakers;

    public Wave() {
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public Speaker[] getSpeakers() {
        return speakers;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSpeakers(Speaker[] speakers) {
        this.speakers = speakers;
    }
}
