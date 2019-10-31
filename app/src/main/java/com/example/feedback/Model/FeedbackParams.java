package com.example.feedback.Model;

public class FeedbackParams {
    Long param1;
    Long param2;
    Long param3;
    Long param4;

    public FeedbackParams() {
    }

    public FeedbackParams(Long param1, Long param2, Long param3, Long param4) {
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
    }

    public Long getParam1() {
        return param1;
    }

    public void setParam1(Long param1) {
        this.param1 = param1;
    }

    public Long getParam2() {
        return param2;
    }

    public void setParam2(Long param2) {
        this.param2 = param2;
    }

    public Long getParam3() {
        return param3;
    }

    public void setParam3(Long param3) {
        this.param3 = param3;
    }

    public Long getParam4() {
        return param4;
    }

    public void setParam4(Long param4) {
        this.param4 = param4;
    }
}
