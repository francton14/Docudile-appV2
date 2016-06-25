package com.docudile.app.services;

import com.docudile.app.data.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

/**
 * Created by franc on 5/23/2016.
 */
public interface ProfileService {

    public String accessBase(Integer id, Model model);

    public String accessManage(Integer id, Model model);

    public String accessDropbox(Integer id, Model model);

    public String dropboxAuthStart();

    public String dropboxAuthFinish(Map<String, String[]> parameterMap, Integer id);

    public ResponseEntity<?> modifyEmail(ModifyEmail modifyEmail, BindingResult result, Integer id);

    public ResponseEntity<?> modifyPassword(ModifyPassword modifyPassword, BindingResult result, Integer id);

    public ResponseEntity<?> modifyFirstname(ModifyFirstname modifyFirstname, BindingResult result, Integer id);

    public ResponseEntity<?> modifyLastname(ModifyLastname modifyLastname, BindingResult result, Integer id);

    public ResponseEntity<?> modifyOrganization(ModifyOrganization modifyOrganization, BindingResult result, Integer id);

}
