package com.docudile.app.services.utils;

import com.docudile.app.data.dto.PasswordObject;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by franc on 5/20/2016.
 */
@Component("passwordValidator")
public class PasswordValidator implements Validator {

    private final String PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}";

    @Override
    public boolean supports(Class<?> aClass) {
        return PasswordObject.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        PasswordObject passwordObject = (PasswordObject) object;
        if (!passwordObject.getPassword().matches(PASSWORD_PATTERN)) {
            errors.rejectValue("passwordObject.password", "messages.password_req");
        }
        if (!passwordObject.getPassword().equals(passwordObject.getConfirmPassword())) {
            errors.rejectValue("passwordObject.confirmPassword", "messages.conf_password_eq");
        }
    }

}
