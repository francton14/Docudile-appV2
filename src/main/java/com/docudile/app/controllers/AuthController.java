package com.docudile.app.controllers;

import com.docudile.app.data.dto.UserRegistration;
import com.docudile.app.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by franc on 2/8/2016.
 */
@Controller
public class AuthController {

    @Autowired
    private RegistrationService registrationService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView goLogin(
            @RequestParam(name = "error", required = false) boolean error) {
        ModelAndView mav = new ModelAndView("login");
        if (error) {
            mav.addObject("error", "There seems to be a problem. Please check your email and password.");
        }
        return mav;
    }
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView goRegister() {
        return registrationService.accessRegistration();
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, headers = "content-type=application/x-www-form-urlencoded")
    public String submitRegistration(@ModelAttribute("userRegistration") @Validated UserRegistration user, BindingResult bindingResult) {
        return registrationService.register(user, bindingResult);
    }

}
