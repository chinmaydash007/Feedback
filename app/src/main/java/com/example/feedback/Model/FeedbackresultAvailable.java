package com.example.feedback.Model;

public class FeedbackresultAvailable {
    String batch;
    String branch;
    String section;

    public FeedbackresultAvailable(String batch, String branch, String section) {
        this.batch = batch;
        this.branch = branch;
        this.section = section;
    }

    public FeedbackresultAvailable() {
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
