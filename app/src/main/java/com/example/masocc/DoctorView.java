package com.example.masocc;

public class DoctorView {

    private User user;
    private ExerciseRecord exerciseRecord;
    private String key;

    public DoctorView(User u, ExerciseRecord er, String key){
        this.user = u;
        this.exerciseRecord = er;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ExerciseRecord getExerciseRecord() {
        return exerciseRecord;
    }

    public void setExerciseRecord(ExerciseRecord exerciseRecord) {
        this.exerciseRecord = exerciseRecord;
    }
}
