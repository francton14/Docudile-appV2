package com.docudile.app.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Created by franc on 5/4/2016.
 */
public class OcrSdkTask {

    @JacksonXmlProperty(isAttribute = true)
    private String id;

    @JacksonXmlProperty(isAttribute = true)
    private String registrationTime;

    @JacksonXmlProperty(isAttribute = true)
    private String statusChangeTime;

    @JacksonXmlProperty(isAttribute = true)
    private String status;

    @JacksonXmlProperty(isAttribute = true)
    private Integer filesCount;

    @JacksonXmlProperty(isAttribute = true)
    private Integer credits;

    @JacksonXmlProperty(isAttribute = true)
    private Integer estimatedProcessingTime;

    @JacksonXmlProperty(isAttribute = true)
    private String resultUrl;

    @JacksonXmlProperty(isAttribute = true)
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getStatusChangeTime() {
        return statusChangeTime;
    }

    public void setStatusChangeTime(String statusChangeTime) {
        this.statusChangeTime = statusChangeTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getFilesCount() {
        return filesCount;
    }

    public void setFilesCount(Integer filesCount) {
        this.filesCount = filesCount;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Integer getEstimatedProcessingTime() {
        return estimatedProcessingTime;
    }

    public void setEstimatedProcessingTime(Integer estimatedProcessingTime) {
        this.estimatedProcessingTime = estimatedProcessingTime;
    }

    public String getResultUrl() {
        return resultUrl;
    }

    public void setResultUrl(String resultUrl) {
        this.resultUrl = resultUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
