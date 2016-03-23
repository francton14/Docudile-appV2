package com.docudile.app.controllers;

import com.docudile.app.data.dao.UserDao;
import com.docudile.app.data.dto.FileShowDto;
import com.docudile.app.data.dto.FolderShowDto;
import com.docudile.app.data.dto.GeneralMessageResponseDto;
import com.docudile.app.data.dto.SyncResponseDto;
import com.docudile.app.services.DocumentService;
import com.docudile.app.services.DropboxService;
import com.docudile.app.services.FileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

/**
 * Created by franc on 2/6/2016.
 */
@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private DocumentService documentService;

    @RequestMapping()
    public ModelAndView goHome(Principal principal) {
        return documentService.home(principal.getName());
    }

    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public @ResponseBody GeneralMessageResponseDto uploadDoc(@RequestParam("document") MultipartFile document, Principal principal) {
        return documentService.classifyThenUpload(document, principal.getName());
    }

    @RequestMapping(value = "/file/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody FileSystemResource downloadFile(@PathVariable("id") Integer id, Principal principal) {
        return documentService.showFile(id, principal.getName());
    }

    @RequestMapping(value = "/folder")
    public @ResponseBody List<FolderShowDto> showRoot(Principal principal) {
        return documentService.showRoot(principal.getName());
    }

    @RequestMapping(value = "/folder/{id}")
    public @ResponseBody FolderShowDto showFolder(@PathVariable("id") Integer folderId, Principal principal) {
        return documentService.showFolder(folderId, principal.getName());
    }

    @RequestMapping(value = "/sync")
    public @ResponseBody SyncResponseDto sync(Principal principal) {
        return documentService.syncToDropbox(principal.getName());
    }

}
