package Entities;

/**
 * Created by aakashgupa1236 on 30/9/2016.
 */
public class User {
    String username;
    String name;
    String password;
    String contact;

    public User(String username, String name, String password, String contact) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.contact = contact;
    }

    public User(){
        this("", "", "", "");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
