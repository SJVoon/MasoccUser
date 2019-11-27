package com.example.masocc;

import java.util.List;

public class ExerciseRecord {
    private String date, type, feeling;
    private List<String> time;

    public ExerciseRecord(String date, String type, List<String> time, String feeling){
        this.date = date;
        this.type = type;
        this.time = time;
        this.feeling = feeling;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public List<String> getTime() {
        return time;
    }

    public String getFeeling() {
        return feeling;
    }
}