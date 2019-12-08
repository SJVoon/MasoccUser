package com.example.masocc;

public class AssessmentView {

    private User user;
    private AssessmentRecord assessmentRecord;
    private String key;

    public AssessmentView(User u, AssessmentRecord er, String key){
        this.user = u;
        this.assessmentRecord = er;
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

    public AssessmentRecord getAssessmentRecord() {
        return assessmentRecord;
    }

    public void setAssessmentRecord(AssessmentRecord assessmentRecord) {
        this.assessmentRecord = assessmentRecord;
    }
}
