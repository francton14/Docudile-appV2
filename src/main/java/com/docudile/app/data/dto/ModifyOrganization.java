package com.docudile.app.data.dto;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by franc on 5/25/2016.
 */
public class ModifyOrganization {

    @NotEmpty
    private String organization;

    @NotEmpty
    private String password;

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
