package com.example.masocc;

import java.util.List;

public class AssessmentRecord {
    private String date, comment;
    private List<String> feeling;

    public AssessmentRecord(){}

    public AssessmentRecord(String date, List<String> feeling){
        this.date = date;
        this.feeling = feeling;
        this.comment = "";
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setFeeling(List<String> feeling) {
        this.feeling = feeling;
    }

    public List<String> getFeeling() {
        return feeling;
    }

}
