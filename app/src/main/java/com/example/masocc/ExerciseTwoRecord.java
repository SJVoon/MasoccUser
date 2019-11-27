package com.example.masocc;

public class ExerciseTwoRecord {
    private String date, type, time, feeling;

    public ExerciseTwoRecord(String date, String type, String time, String feeling){
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

    public String getTime() {
        return time;
    }

    public String getFeeling() {
        return feeling;
    }
}
