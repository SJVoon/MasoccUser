package com.example.masocc;

import java.util.ArrayList;
import java.util.List;

public class ExerciseRecord {
    private String date, type, feeling;
    private List<String> time;

    public ExerciseRecord(){

    }

//    public ExerciseRecord(String date, String type, List<String> time, String feeling,String comment, String data){
//        this.date = date;
//        this.type = type;
//        this.time = time;
//        this.feeling = feeling;
//        this.comment = comment;
//        this.data = data;
//    }

    public ExerciseRecord(String date, String type, List<String> time, String feeling){
        this.date = date;
        this.type = type;
        this.time = time;
        this.feeling = feeling;
    }

    public void setDate(String date) { this.date = date; }

    public void setType(String type) { this.type = type; }

    public void setFeeling(String feeling) { this.feeling = feeling; }

    public void setTime(List<String> time) { this.time = time; }

    public String getDate() { return date; }

    public String getType() { return type; }

    public List<String> getTime() { return time; }

    public String getFeeling() { return feeling; }

}
