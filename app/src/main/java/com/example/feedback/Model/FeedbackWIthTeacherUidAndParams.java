package com.example.feedback.Model;

public class FeedbackWIthTeacherUidAndParams {
    String name;
    FeedbackParams feedbackParams;

    public FeedbackWIthTeacherUidAndParams() {
    }

    public FeedbackWIthTeacherUidAndParams(String name, FeedbackParams feedbackParams) {
        this.name = name;
        this.feedbackParams = feedbackParams;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FeedbackParams getFeedbackParams() {
        return feedbackParams;
    }

    public void setFeedbackParams(FeedbackParams feedbackParams) {
        this.feedbackParams = feedbackParams;
    }
}
