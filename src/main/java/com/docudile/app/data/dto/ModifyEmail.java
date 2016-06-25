package com.docudile.app.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by franc on 5/23/2016.
 */
public class ModifyEmail {

    @Email
    @NotEmpty
    @JsonProperty
    private String oldEmail;

    @Valid
    @NotNull
    @JsonProperty
    private EmailObject emailObject;

    public String getOldEmail() {
        return oldEmail;
    }

    public void setOldEmail(String oldEmail) {
        this.oldEmail = oldEmail;
    }

    public EmailObject getEmailObject() {
        return emailObject;
    }

    public void setEmailObject(EmailObject emailObject) {
        this.emailObject = emailObject;
    }

    @Override
    public String toString() {
        return "ModifyEmail{" +
                "oldEmail='" + oldEmail + '\'' +
                ", emailObject=" + emailObject +
                '}';
    }

}
