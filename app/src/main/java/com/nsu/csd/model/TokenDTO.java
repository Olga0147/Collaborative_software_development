package com.nsu.csd.model;

import com.google.gson.annotations.SerializedName;

public class TokenDTO {

    /**
     * token : db46cd96-3733-4266-acba-84aabbc8f218
     */

    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
