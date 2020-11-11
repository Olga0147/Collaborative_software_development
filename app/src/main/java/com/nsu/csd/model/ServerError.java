package com.nsu.csd.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerError {

    @SerializedName("errorCode")
    private Integer errorCode;
    @SerializedName("message")
    private String message;
    @SerializedName("time")
    private String time;
    @SerializedName("details")
    private List<String> details;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}
