package com.docudile.app.services.impl;

import com.docudile.app.data.dao.UserDao;
import com.docudile.app.data.dto.UserRegistration;
import com.docudile.app.data.entities.User;
import com.docudile.app.services.RegistrationService;
import com.docudile.app.services.utils.EmailValidator;
import com.docudile.app.services.utils.PasswordValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by franc on 2/7/2016.
 */
@Service("registrationService")
@Transactional
public class RegistrationServiceImpl implements RegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Override
    public ModelAndView accessRegistration() {
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("userRegistration", new UserRegistration());
        return mav;
    }

    public String register(UserRegistration user, BindingResult result) {
        new PasswordValidator().validate(user.getPasswordObject(), result);
        new EmailValidator().validate(user.getEmailObject(), result);
        logger.debug("Registration Error: " + result.hasErrors());
        if (!result.hasErrors()) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            User userEntity = new User();
            userEntity.setLastname(user.getLastname());
            userEntity.setFirstname(user.getFirstname());
            userEntity.setEmail(user.getEmailObject().getEmail());
            userEntity.setPassword(passwordEncoder.encode(user.getPasswordObject().getPassword()));
            userEntity.setOrganization(user.getOrganization());
            if (userDao.create(userEntity)) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmailObject().getEmail(), user.getPasswordObject().getPassword(), authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
                return "redirect:/";
            }
        }
        return "register";
    }

}
