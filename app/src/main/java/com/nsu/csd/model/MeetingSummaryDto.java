package com.nsu.csd.model;

import com.google.gson.annotations.SerializedName;

public class MeetingSummaryDto {

    /**
     * end : 2020-12-23
     * id : 3fa85f64-5717-4562-b3fc-2c963f66afa6
     * start : 2020-12-23
     * title : string
     */

    @SerializedName("end")
    private String end;
    @SerializedName("id")
    private String id;
    @SerializedName("start")
    private String start;
    @SerializedName("title")
    private String title;

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
