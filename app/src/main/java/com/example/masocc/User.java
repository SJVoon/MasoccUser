package com.example.masocc;

public class User {
    private String username, email, icNumber,handphoneNumber, password, fullName;

    public User(){

    }

    public User(String fullName, String email, String icNumber, String handphoneNumber, String password) {
        this.fullName = fullName;
        this.email = email;
        this.icNumber = icNumber;
        this.handphoneNumber = handphoneNumber;
        this.password = password;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }

    public User(String username, String fullName, String email, String icNumber, String handphoneNumber, String password) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.icNumber = icNumber;
        this.handphoneNumber = handphoneNumber;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }

    public void setHandphoneNumber(String handphoneNumber) {
        this.handphoneNumber = handphoneNumber;
    }

    public String getHandphoneNumber() {
        return handphoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIcNumber() {
        return icNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
