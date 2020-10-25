package com.quarantino.ruto.HelperClasses;

public class UserHelperClassFirebase {
    String name, username, password, email;

    public UserHelperClassFirebase(String userName, String userUsername, String userEmail, String userPassword) {
        this.name = userName;
        this.username = userUsername;
        this.email = userEmail;
        this.password = userPassword;
    }

    public UserHelperClassFirebase() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
