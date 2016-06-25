package com.docudile.app.services.utils;

import com.docudile.app.data.dto.EmailObject;
import com.docudile.app.data.dto.PasswordObject;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by franc on 5/20/2016.
 */
@Component("emailValidator")
public class EmailValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return PasswordObject.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        EmailObject emailObject= (EmailObject) object;
        if (!emailObject.getEmail().equals(emailObject.getConfirmEmail())) {
            errors.rejectValue("emailObject.confirmEmail", "messages.conf_email_eq");
        }
    }

}
