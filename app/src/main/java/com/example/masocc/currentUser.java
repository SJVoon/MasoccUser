package com.example.masocc;

class currentUser {
    private static currentUser ourInstance;

    //singleton
    public static void init_Instance(){
        if(ourInstance == null){
            ourInstance = new currentUser();
        }
    }

    public static currentUser getInstance() {
        return ourInstance;
    }

    private User thisUser;

    private currentUser() {
    }

    public void setUser(User u){
        this.thisUser = u;
    }

    public User getUser(){
        return this.thisUser;
    }
}
