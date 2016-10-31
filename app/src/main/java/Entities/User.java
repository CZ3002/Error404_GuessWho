package Entities;

/**
 * Created by aakashgupa1236 on 30/9/2016.
 */
public class User {
    String username;
    String name;
    String password;
    String contact;
    String DOB;

    /**
     * Constructor for the class. Initializes a user.
     *
     * @param username username of the user
     * @param name name of the user
     * @param password desired password
     * @param contact contact number of the user
     * @param DOB user's date of birth
     */
    public User(String username, String name, String password, String contact, String DOB) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.contact = contact;
        this.DOB = DOB;
    }

    /**
     * Constructor for the class. Initializes a user with empty fields.
     */
    public User(){
        this("", "", "", "", "");
    }

    /**
     * Getter for the username
     * @return String username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for the username
     * @param username String username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for the user's name
     * @return String user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the user's name
     * @param name String user's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the user's password
     * @return String user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for the user's password
     * @param password String user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for the user's contact details
     * @return String user's contact details
     */
    public String getContact() {
        return contact;
    }

    /**
     * Setter for the user's contact details
     * @param contact String user's contact details
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * Getter for the user's date of birth
     * @return String user's date of birth
     */
    public String getDOB() {
        return DOB;
    }

    /**
     * Setter for the user's date of birth
     * @param DOB String user's date of birth
     */
    public void setDOB(String DOB) {
        this.DOB = DOB;
    }
}
