package com.example.masocc;

import java.util.List;

public class AssessmentRecord {
    private String date;
    private List<String> feeling;

    public AssessmentRecord(){}

    public AssessmentRecord(String date, List<String> feeling){
        this.date = date;
        this.feeling = feeling;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFeeling(List<String> feeling) {
        this.feeling = feeling;
    }

    public List<String> getFeeling() {
        return feeling;
    }

}
