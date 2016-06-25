package com.docudile.app.services;

import com.docudile.app.data.dto.UserRegistration;
import com.docudile.app.data.entities.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by franc on 2/7/2016.
 */
public interface RegistrationService {

    public ModelAndView accessRegistration();

    public String register(UserRegistration user, BindingResult result);

}
