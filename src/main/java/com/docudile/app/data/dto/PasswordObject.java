package com.docudile.app.data.dto;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by franc on 5/23/2016.
 */
public class PasswordObject {

    @NotEmpty
    private String password;

    @NotEmpty
    private String confirmPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}
