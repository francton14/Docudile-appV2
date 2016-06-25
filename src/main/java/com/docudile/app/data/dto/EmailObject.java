package com.docudile.app.data.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by franc on 5/23/2016.
 */
public class EmailObject {

    @Email
    @NotEmpty
    private String email;

    @Email
    @NotEmpty
    private String confirmEmail;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmEmail() {
        return confirmEmail;
    }

    public void setConfirmEmail(String confirmEmail) {
        this.confirmEmail = confirmEmail;
    }

    @Override
    public String toString() {
        return "EmailObject{" +
                "email='" + email + '\'' +
                ", confirmEmail='" + confirmEmail + '\'' +
                '}';
    }

}
