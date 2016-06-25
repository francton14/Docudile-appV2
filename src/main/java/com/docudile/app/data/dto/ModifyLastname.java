package com.docudile.app.data.dto;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by franc on 5/25/2016.
 */
public class ModifyLastname {

    @NotEmpty
    private String lastname;

    @NotEmpty
    private String password;

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
