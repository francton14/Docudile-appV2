package com.docudile.app.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by franc on 2/7/2016.
 */
public class UserRegistration implements Serializable {

    @Valid
    @NotNull
    private EmailObject emailObject;

    @Valid
    @NotNull
    private PasswordObject passwordObject;

    @NotEmpty
    private String firstname;

    @NotEmpty
    private String lastname;

    @NotEmpty
    private String organization;

    public EmailObject getEmailObject() {
        return emailObject;
    }

    public void setEmailObject(EmailObject emailObject) {
        this.emailObject = emailObject;
    }

    public PasswordObject getPasswordObject() {
        return passwordObject;
    }

    public void setPasswordObject(PasswordObject passwordObject) {
        this.passwordObject = passwordObject;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

}
