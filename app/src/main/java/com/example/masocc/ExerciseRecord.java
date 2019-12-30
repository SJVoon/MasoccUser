package com.example.masocc;

import java.util.ArrayList;
import java.util.List;

public class ExerciseRecord {
    private String date, type, feeling, comment, uri;
    private List<String> time, pulseData, pedoData;

    public List<String> getPulseData() {
        return pulseData;
    }

    public void setPulseData(List<String> pulseData) {
        this.pulseData = pulseData;
    }

    public List<String> getPedoData() {
        return pedoData;
    }

    public void setPedoData(List<String> pedoData) {
        this.pedoData = pedoData;
    }

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

    public ExerciseRecord(String date, String type, List<String> time){
        this.date = date;
        this.type = type;
        this.time = time;
        this.feeling = "";
        this.comment = "";
        this.pulseData = new ArrayList<>();
        this.pulseData.add("90");
        this.pedoData = new ArrayList<>();
        this.pedoData.add("50");
        this.uri = "";
    }

    public void setDate(String date) { this.date = date; }

    public void setType(String type) { this.type = type; }

    public void setFeeling(String feeling) { this.feeling = feeling; }

    public void setTime(List<String> time) { this.time = time; }

    public void setComment(String comment){this.comment = comment;}

    public String getComment(){return comment;}

    public String getDate() { return date; }

    public String getType() { return type; }

    public List<String> getTime() { return time; }

    public String getFeeling() { return feeling; }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

//    public boolean isStart() {
//        return start;
//    }
//
//    public void setStart(boolean start) {
//        this.start = start;
//    }
//
//    public boolean isFinish() {
//        return finish;
//    }
//
//    public void setFinish(boolean finish) {
//        this.finish = finish;
//    }
}
