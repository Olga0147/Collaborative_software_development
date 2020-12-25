package com.nsu.csd.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MeetingInfoDto {

    /**
     * days : [{"busyness":"HIGH"}]
     * end : 2020-12-23
     * id : 3fa85f64-5717-4562-b3fc-2c963f66afa6
     * inviteKey : string
     * participants : [{"firstName":"string","lastName":"string"}]
     * start : 2020-12-23
     * title : string
     */

    @SerializedName("end")
    private String end;
    @SerializedName("id")
    private String id;
    @SerializedName("inviteKey")
    private String inviteKey;
    @SerializedName("start")
    private String start;
    @SerializedName("title")
    private String title;
    @SerializedName("days")
    private List<DaysDTO> days;
    @SerializedName("participants")
    private List<ParticipantsDTO> participants;

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

    public String getInviteKey() {
        return inviteKey;
    }

    public void setInviteKey(String inviteKey) {
        this.inviteKey = inviteKey;
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

    public List<DaysDTO> getDays() {
        return days;
    }

    public void setDays(List<DaysDTO> days) {
        this.days = days;
    }

    public List<ParticipantsDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantsDTO> participants) {
        this.participants = participants;
    }

    public static class DaysDTO {
        /**
         * busyness : HIGH
         */

        @SerializedName("busyness")
        private String busyness;

        public String getBusyness() {
            return busyness;
        }

        public void setBusyness(String busyness) {
            this.busyness = busyness;
        }
    }

    public static class ParticipantsDTO {
        /**
         * firstName : string
         * lastName : string
         */

        @SerializedName("firstName")
        private String firstName;
        @SerializedName("lastName")
        private String lastName;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }
}
