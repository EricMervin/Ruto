package com.quarantino.ruto.HelperClasses;

public class userHelperClassFirebase {
    String name, username, password;

    public userHelperClassFirebase(String userName, String userUsername, String userPassword) {
        this.name = userName;
        this.username = userUsername;
        this.password = userPassword;
    }

    public userHelperClassFirebase() {
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
}
