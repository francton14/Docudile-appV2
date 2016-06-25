package com.docudile.app.data.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by franc on 5/23/2016.
 */
public class ModifyPassword {

    @NotEmpty
    private String oldPassword;

    @Valid
    @NotNull
    private PasswordObject passwordObject;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public PasswordObject getPasswordObject() {
        return passwordObject;
    }

    public void setPasswordObject(PasswordObject passwordObject) {
        this.passwordObject = passwordObject;
    }

}
