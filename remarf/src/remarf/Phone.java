/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package remarf;

/**
 *
 * @author mauricio
 */
public class Phone {

    private int id;
    private String number;
    private Speaker[] speakers;

    public Phone() {
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Speaker[] getSpeakers() {
        return speakers;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setSpeakers(Speaker[] speakers) {
        this.speakers = speakers;
    }

    
}
