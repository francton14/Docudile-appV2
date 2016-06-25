package com.docudile.app.data.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Created by franc on 5/4/2016.
 */
@JacksonXmlRootElement(localName = "response")
public class OcrSdkTaskResponse {

    @JacksonXmlProperty(localName = "task")
    private OcrSdkTask ocrSdkTask;

    public OcrSdkTask getOcrSdkTask() {
        return ocrSdkTask;
    }

    public void setOcrSdkTask(OcrSdkTask ocrSdkTask) {
        this.ocrSdkTask = ocrSdkTask;
    }

}
