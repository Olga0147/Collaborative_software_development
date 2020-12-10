package com.nsu.csd.model;

import com.google.gson.annotations.SerializedName;

public class NewEventDTO {

    /**
     * description : string
     * end : {"dateTime":"2020-12-02T10:33:45.601Z","timeZone":"string"}
     * importance : HIGH
     * recurrenceRule : string
     * start : {"dateTime":"2020-12-02T10:33:45.601Z","timeZone":"string"}
     * title : string
     */

    @SerializedName("description")
    private String description;
    @SerializedName("end")
    private EndDTO end;
    @SerializedName("importance")
    private String importance;
    @SerializedName("recurrenceRule")
    private String recurrenceRule;
    @SerializedName("start")
    private StartDTO start;
    @SerializedName("title")
    private String title;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EndDTO getEnd() {
        return end;
    }

    public void setEnd(EndDTO end) {
        this.end = end;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getRecurrenceRule() {
        return recurrenceRule;
    }

    public void setRecurrenceRule(String recurrenceRule) {
        this.recurrenceRule = recurrenceRule;
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
         * dateTime : 2020-12-02T10:33:45.601Z
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
         * dateTime : 2020-12-02T10:33:45.601Z
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
