package Entities;

import java.io.Serializable;

/**
 * Created by aakashgupta1236 on 30/9/2016.
 */
public class Acquaintance implements Serializable {
    String username;
    String acqName;
    String relationship;
    String contact;
    String notes;
    String base64;
    String soundFile;
    String pos1;
    String pos2;
    String pos3;
    String pos4;
    String pos5;

    /**
     * Constructor for the class. Initializes an acquaintance.
     *
     * @param username the user signed in
     * @param acqName name of the acquaintance
     * @param relationship relationship with the user
     * @param contact contact details of the acquaintance
     * @param notes additional notes
     * @param base64 string of the image
     * @param soundFile location of the sound file
     * @param pos1 positions of the first sentence
     * @param pos2 positions of the second sentence
     * @param pos3 positions of the third sentence
     * @param pos4 positions of the fourth sentence
     * @param pos5 positions of the last sentence
     */
    public Acquaintance(String username, String acqName, String relationship, String contact, String notes, String base64, String soundFile, String pos1, String pos2, String pos3, String pos4, String pos5) {
        this.username = username;
        this.acqName = acqName;
        this.relationship = relationship;
        this.contact = contact;
        this.notes = notes;
        this.base64 = base64;
        this.soundFile = soundFile;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.pos3 = pos3;
        this.pos4 = pos4;
        this.pos5 = pos5;
    }

    /**
     * Constructor for the class. Initializes an acquaintance with empty fields.
     */
    public Acquaintance() {
        this("", "", "", "", "", "", "", "", "", "", "", "");
    }

    /**
     * Getter for username
     * @return String name of the user whose acquaintance it is
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for username
     * @param username String name of the user whose acquaintance it is
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for the acquaintance name
     * @return String name of the acquaintance
     */
    public String getAcqName() {
        return acqName;
    }

    /**
     * Setter for the acquaintance name
     * @param acqName String name of the acquaintance
     */
    public void setAcqName(String acqName) {
        this.acqName = acqName;
    }

    /**
     * Getter for relationship with the user
     * @return String relation with the user
     */
    public String getRelationship() {
        return relationship;
    }

    /**
     * Setter for relationship with the user
     * @param relationship relation with the user
     */
    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    /**
     * Getter for the contact of the acquaintance
     * @return String contact of the acquaintance
     */
    public String getContact() {
        return contact;
    }

    /**
     * Setter for the contact of the acquaintance
     * @param contact String contact of the acquaintance
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * Getter for additional notes
     * @return String additional notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Setter for additional notes
     * @param notes String for additional notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Getter for the string of the image
     * @return String base64 value of the image
     */
    public String getBase64() {
        return base64;
    }

    /**
     * Setter for the string of the image
     * @param base64 String base64 value of the image
     */
    public void setBase64(String base64) {
        this.base64 = base64;
    }

    /**
     * Getter for the location of sound file
     * @return String location
     */
    public String getSoundFile() {
        return soundFile;
    }

    /**
     * Setter for the location of the sound file
     * @param soundFile String location
     */
    public void setSoundFile(String soundFile) {
        this.soundFile = soundFile;
    }

    /**
     * Getter for the first position
     * @return String timings
     */
    public String getPos1() {
        return pos1;
    }

    /**
     * Setter for the first position
     * @param pos1 String timings
     */
    public void setPos1(String pos1) {
        this.pos1 = pos1;
    }

    /**
     * Getter for the second position
     * @return String timings
     */
    public String getPos2() {
        return pos2;
    }

    /**
     * Setter for the second position
     * @param pos2 String timings
     */
    public void setPos2(String pos2) {
        this.pos2 = pos2;
    }

    /**
     * Getter for the third position
     * @return String timings
     */
    public String getPos3() {
        return pos3;
    }

    /**
     * Setter for the third position
     * @param pos3 String timings
     */
    public void setPos3(String pos3) {
        this.pos3 = pos3;
    }

    /**
     * Getter for the fourth position
     * @return String timings
     */
    public String getPos4() {
        return pos4;
    }

    /**
     * Setter for the fourth position
     * @param pos4
     */
    public void setPos4(String pos4) {
        this.pos4 = pos4;
    }

    /**
     * Getter for the last position
     * @return String timings
     */
    public String getPos5() {
        return pos5;
    }

    /**
     * Getter for the last position
     * @param pos5 String timings
     */
    public void setPos5(String pos5) {
        this.pos5 = pos5;
    }
}