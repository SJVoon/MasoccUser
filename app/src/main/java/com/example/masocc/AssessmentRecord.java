package com.example.masocc;

import java.util.List;

public class AssessmentRecord {
    private String date;
    private List<String> feeling;

    public AssessmentRecord(String date, List<String> feeling){
        this.date = date;
        this.feeling = feeling;
    }

    public String getDate() {
        return date;
    }

    public List<String> getFeeling() {
        return feeling;
    }

}
