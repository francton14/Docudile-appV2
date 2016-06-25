package com.docudile.app.data.dto;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by franc on 5/25/2016.
 */
public class ModifyFirstname {

    @NotEmpty
    private String firstname;

    @NotEmpty
    private String password;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
