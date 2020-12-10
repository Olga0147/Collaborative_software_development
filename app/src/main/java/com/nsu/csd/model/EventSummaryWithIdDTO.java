package com.nsu.csd.model;

import com.google.gson.annotations.SerializedName;

public class EventSummaryWithIdDTO {


    /**
     * end : {"dateTime":"2020-11-25T17:21:43.588Z","timeZone":"string"}
     * id : 3fa85f64-5717-4562-b3fc-2c963f66afa6
     * start : {"dateTime":"2020-11-25T17:21:43.588Z","timeZone":"string"}
     * title : string
     */

    @SerializedName("end")
    private EndDTO end;
    @SerializedName("id")
    private String id;
    @SerializedName("start")
    private StartDTO start;
    @SerializedName("title")
    private String title;

    public EndDTO getEnd() {
        return end;
    }

    public void setEnd(EndDTO end) {
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StartDTO getStart() {
        return start;
    }

    public void setStart(StartDTO start) {
        this.start = start;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static class EndDTO {
        /**
         * dateTime : 2020-11-25T17:21:43.588Z
         * timeZone : string
         */

        @SerializedName("dateTime")
        private String dateTime;
        @SerializedName("timeZone")
        private String timeZone;

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
        }
    }

    public static class StartDTO {
        /**
         * dateTime : 2020-11-25T17:21:43.588Z
         * timeZone : string
         */

        @SerializedName("dateTime")
        private String dateTime;
        @SerializedName("timeZone")
        private String timeZone;

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
        }
    }
}
