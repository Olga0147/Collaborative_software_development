package com.nsu.csd.model;

import com.google.gson.annotations.SerializedName;

public class NewMeetingDto {

    /**
     * end : 2020-12-23
     * start : 2020-12-23
     * timeZone : string
     * title : string
     */

    @SerializedName("end")
    private String end;
    @SerializedName("start")
    private String start;
    @SerializedName("timeZone")
    private String timeZone;
    @SerializedName("title")
    private String title;

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
