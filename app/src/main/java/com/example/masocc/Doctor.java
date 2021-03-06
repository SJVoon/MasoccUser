package com.example.masocc;

public class Doctor {
    private String username, email, icNumber,handphoneNumber, password, fullName;

    private static Doctor ourInstance;

    public static Doctor getInstance() {
        if(ourInstance == null){
            ourInstance = new Doctor();
        }
        return ourInstance;
    }

    private Doctor(){
    }

    public void setDoctor(Doctor d) {
        this.username = d.username;
        this.fullName = d.fullName;
        this.email = d.email;
        this.icNumber = d.icNumber;
        this.handphoneNumber = d.handphoneNumber;
        this.password = d.password;
    }

    public void setDoctor(String username, String fullName, String email, String icNumber, String handphoneNumber, String password) {
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

    public void setHandphoneNumber(String handphoneNumber) { this.handphoneNumber = handphoneNumber; }

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

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}
