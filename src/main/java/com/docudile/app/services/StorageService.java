package com.docudile.app.services;

import com.docudile.app.data.dto.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by franc on 2/10/2016.
 */
public interface StorageService {

    public ModelAndView accessStorage();

    public ModelAndView accessUpload(String username);

    public ModelAndView accessSearch(String username);

    public GeneralMessageResponse storeFile(MultipartFile file, String username);

    public List<FolderShow> getRootFolders(String username);

    public FolderShow getFolder(Integer id, String username);

    public FileSystemResource download(Integer id, String username);

    public GeneralMessageResponse delete(Integer id, String username);

    public SyncResponse syncDropbox(String username);

    public ResponseEntity<?> search(Search search, BindingResult result, String username);

}
