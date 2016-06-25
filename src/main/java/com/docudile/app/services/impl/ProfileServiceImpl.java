package com.docudile.app.services.impl;

import com.docudile.app.data.dao.DocumentDao;
import com.docudile.app.data.dao.UserDao;
import com.docudile.app.data.dto.*;
import com.docudile.app.data.entities.User;
import com.docudile.app.exceptions.InvalidRequestException;
import com.docudile.app.services.DropboxService;
import com.docudile.app.services.ProfileService;
import com.docudile.app.services.utils.EmailValidator;
import com.docudile.app.services.utils.PasswordValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Map;

/**
 * Created by franc on 5/23/2016.
 */
@Service("profileService")
@Transactional
@PropertySource({"classpath:/messages.properties"})
public class ProfileServiceImpl implements ProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private DocumentDao documentDao;

    @Autowired
    private DropboxService dropboxService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String accessBase(Integer id, Model model) {
        User user = userDao.show(id);
        Long documentCount = documentDao.getDocumentCount(user);
        Long totalSize = documentDao.getTotalSize(user);
        model.addAttribute("user", user);
        model.addAttribute("sideNav", "profile");
        model.addAttribute("profileNav", "info");
        model.addAttribute("numberOfDocuments", documentCount);
        model.addAttribute("totalSize", totalSize);
        return "profile";
    }

    @Override
    public String accessManage(Integer id, Model model) {
        User user = userDao.show(id);
        model.addAttribute("user", user);
        model.addAttribute("sideNav", "profile");
        model.addAttribute("profileNav", "manage");
        return "profile.manage";
    }

    @Override
    public String accessDropbox(Integer id, Model model) {
        User user = userDao.show(id);
        model.addAttribute("user", user);
        model.addAttribute("sideNav", "profile");
        model.addAttribute("profileNav", "dropbox");
        if (StringUtils.isEmpty(user.getDropboxAccessToken())) {
            return "profile.dropbox.setup";
        } else {
            DropboxUserInfo dropboxUserInfo = new DropboxUserInfo();
            dropboxUserInfo.setName(dropboxService.name(user.getDropboxAccessToken()));
            dropboxUserInfo.setEmail(dropboxService.getEmail(user.getDropboxAccessToken()));
            dropboxUserInfo.setEmailVerified(dropboxService.isEmailVerified(user.getDropboxAccessToken()));
            dropboxUserInfo.setAccountType(WordUtils.capitalizeFully(dropboxService.accountType(user.getDropboxAccessToken())));
            dropboxUserInfo.setPaired(dropboxService.isPaired(user.getDropboxAccessToken()));
            dropboxUserInfo.setUsedSpace(dropboxService.getUsedSpace(user.getDropboxAccessToken()));
            dropboxUserInfo.setTotalSpace(dropboxService.getTotalSpace(user.getDropboxAccessToken()));
            if (dropboxUserInfo.getPaired()) {
                dropboxUserInfo.setTeamName(dropboxService.teamName(user.getDropboxAccessToken()));
            }
            model.addAttribute("dropbox", dropboxUserInfo);
            return "profile.dropbox";
        }
    }

    @Override
    public String dropboxAuthStart() {
        return "redirect:" + dropboxService.linkDropbox();
    }

    @Override
    public String dropboxAuthFinish(Map<String, String[]> parameterMap, Integer id) {
        User user = userDao.show(id);
        String dropboxAccessToken = dropboxService.finishAuth(parameterMap);
        user.setDropboxAccessToken(dropboxAccessToken);
        return "redirect:/profile/dropbox";
    }

    @Override
    public ResponseEntity<?> modifyEmail(ModifyEmail modifyEmail, BindingResult result, Integer id) {
        User user = userDao.show(id);
        new EmailValidator().validate(modifyEmail.getEmailObject(), result);
        if (!user.getEmail().equals(modifyEmail.getOldEmail()) && StringUtils.isNotEmpty(modifyEmail.getOldEmail())) {
            result.rejectValue("oldEmail", "messages.old_email");
        }
        if (!result.hasErrors()) {
            user.setEmail(modifyEmail.getEmailObject().getEmail());
            userDao.update(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        throw new InvalidRequestException("Invalid Request.", result);
    }

    @Override
    public ResponseEntity<?> modifyPassword(ModifyPassword modifyPassword, BindingResult result, Integer id) {
        User user = userDao.show(id);
        new PasswordValidator().validate(modifyPassword.getPasswordObject(), result);
        if (!passwordEncoder.matches(modifyPassword.getOldPassword(), user.getPassword()) && StringUtils.isNotEmpty(modifyPassword.getOldPassword())) {
            result.rejectValue("oldPassword", "messages.old_password");
        }
        if (!result.hasErrors()) {
            user.setPassword(passwordEncoder.encode(modifyPassword.getPasswordObject().getPassword()));
            userDao.update(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        throw new InvalidRequestException("Invalid Request.", result);
    }

    @Override
    public ResponseEntity<?> modifyFirstname(ModifyFirstname modifyFirstname, BindingResult result, Integer id) {
        User user = userDao.show(id);
        if (!passwordEncoder.matches(modifyFirstname.getPassword(), user.getPassword()) && StringUtils.isNotEmpty(modifyFirstname.getPassword())) {
            result.rejectValue("password", "messages.old_password");
        }
        if (!result.hasErrors()){
            user.setFirstname(modifyFirstname.getFirstname());
            userDao.update(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        throw new InvalidRequestException("Invalid Request.", result);
    }

    @Override
    public ResponseEntity<?> modifyLastname(ModifyLastname modifyLastname, BindingResult result, Integer id) {
        User user = userDao.show(id);
        if (!passwordEncoder.matches(modifyLastname.getPassword(), user.getPassword()) && StringUtils.isNotEmpty(modifyLastname.getPassword())) {
            result.rejectValue("password", "messages.old_password");
        }
        if (!result.hasErrors()){
            user.setLastname(modifyLastname.getLastname());
            userDao.update(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        throw new InvalidRequestException("Invalid Request.", result);
    }

    @Override
    public ResponseEntity<?> modifyOrganization(ModifyOrganization modifyOrganization, BindingResult result, Integer id) {
        User user = userDao.show(id);
        if (!passwordEncoder.matches(modifyOrganization.getPassword(), user.getPassword()) && StringUtils.isNotEmpty(modifyOrganization.getPassword())) {
            result.rejectValue("password", "messages.old_password");
        }
        if (!result.hasErrors()) {
            user.setOrganization(modifyOrganization.getOrganization());
            userDao.update(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        throw new InvalidRequestException("Invalid Request.", result);
    }

}
