package com.docudile.app.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by franc on 3/22/2016.
 */
public class SyncResponse {

    @JsonProperty
    private String message;

    @JsonProperty
    private List<String> failed;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getFailed() {
        return failed;
    }

    public void setFailed(List<String> failed) {
        this.failed = failed;
    }

}
