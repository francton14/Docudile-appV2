package com.docudile.app.controllers;

import com.docudile.app.data.dto.*;
import com.docudile.app.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by franc on 5/23/2016.
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @RequestMapping()
    private String goBase(Model model) {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return profileService.accessBase(user.getId(), model);
    }

    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    private String goManage(Model model) {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return profileService.accessManage(user.getId(), model);
    }

    @RequestMapping(value = "/manage/email", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> submitEmail(@RequestBody @Validated ModifyEmail modifyEmail, BindingResult result) {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return profileService.modifyEmail(modifyEmail, result, user.getId());
    }

    @RequestMapping(value = "/manage/password", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> submitPassword(@RequestBody @Validated ModifyPassword modifyPassword, BindingResult result) {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return profileService.modifyPassword(modifyPassword, result, user.getId());
    }

    @RequestMapping(value = "/manage/firstname", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> submitFirstname(@RequestBody @Validated ModifyFirstname modifyFirstname, BindingResult result) {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return profileService.modifyFirstname(modifyFirstname, result, user.getId());
    }

    @RequestMapping(value = "/manage/lastname", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> submitLastname(@RequestBody @Validated ModifyLastname modifyLastname, BindingResult result) {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return profileService.modifyLastname(modifyLastname, result, user.getId());
    }

    @RequestMapping(value = "/manage/organization", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> submitOrganization(@RequestBody @Validated ModifyOrganization modifyOrganization, BindingResult result) {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return profileService.modifyOrganization(modifyOrganization, result, user.getId());
    }

    @RequestMapping("/dropbox")
    private String goDropbox(Model model) {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return profileService.accessDropbox(user.getId(), model);
    }

    @RequestMapping("/dropbox/auth/start")
    private String dropboxStart() {
        return profileService.dropboxAuthStart();
    }

    @RequestMapping("dropbox/auth/finish")
    private String dropboxFinish(WebRequest request) {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return profileService.dropboxAuthFinish(request.getParameterMap(), user.getId());
    }

}
