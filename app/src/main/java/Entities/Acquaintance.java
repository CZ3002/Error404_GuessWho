package Entities;

/**
 * Created by aakashgupa1236 on 30/9/2016.
 */
public class Acquaintance {
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

    public Acquaintance() {
        this("", "", "", "", "", "", "", "", "", "", "", "");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAcqName() {
        return acqName;
    }

    public void setAcqName(String acqName) {
        this.acqName = acqName;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getSoundFile() {
        return soundFile;
    }

    public void setSoundFile(String soundFile) {
        this.soundFile = soundFile;
    }

    public String getPos1() {
        return pos1;
    }

    public void setPos1(String pos1) {
        this.pos1 = pos1;
    }

    public String getPos2() {
        return pos2;
    }

    public void setPos2(String pos2) {
        this.pos2 = pos2;
    }

    public String getPos3() {
        return pos3;
    }

    public void setPos3(String pos3) {
        this.pos3 = pos3;
    }

    public String getPos4() {
        return pos4;
    }

    public void setPos4(String pos4) {
        this.pos4 = pos4;
    }

    public String getPos5() {
        return pos5;
    }

    public void setPos5(String pos5) {
        this.pos5 = pos5;
    }
}
