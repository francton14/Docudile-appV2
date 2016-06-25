package com.docudile.app.controllers;

import com.docudile.app.data.dto.*;
import com.docudile.app.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

/**
 * Created by franc on 2/6/2016.
 */
@Controller
public class StorageController {

    @Autowired
    private StorageService storageService;

    @RequestMapping(value = "/")
    public ModelAndView goHome() {
        return storageService.accessStorage();
    }

    @RequestMapping(value = "/upload")
    public ModelAndView goUpload(Principal principal) {
        return storageService.accessUpload(principal.getName());
    }

    @RequestMapping(value = "/search")
    public ModelAndView goSearch(Principal principal) {
        return storageService.accessSearch(principal.getName());
    }

    @RequestMapping(value = "/document", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public @ResponseBody GeneralMessageResponse uploadDoc(@RequestParam("document") MultipartFile document, Principal principal) {
        return storageService.storeFile(document, principal.getName());
    }

    @RequestMapping(value = "/document/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody FileSystemResource downloadFile(@PathVariable("id") Integer id, Principal principal) {
        return storageService.download(id, principal.getName());
    }

    @RequestMapping(value = "/folder", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<FolderShow> showRoot(Principal principal) {
        return storageService.getRootFolders(principal.getName());
    }

    @RequestMapping(value = "/folder/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    FolderShow showFolder(@PathVariable("id") Integer folderId, Principal principal) {
        return storageService.getFolder(folderId, principal.getName());
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseEntity<?> search(@RequestBody @Validated Search search, BindingResult result, Principal principal){
        return storageService.search(search, result, principal.getName());
    }

    @RequestMapping(value = "/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    SyncResponse sync(Principal principal) {
        return storageService.syncDropbox(principal.getName());
    }

}
