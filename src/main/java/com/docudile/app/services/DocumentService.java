package com.docudile.app.services;

import com.docudile.app.data.dto.FolderShowDto;
import com.docudile.app.data.dto.GeneralMessageResponseDto;
import com.docudile.app.data.dto.SyncResponseDto;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by franc on 2/9/2016.
 */
public interface DocumentService {

    public ModelAndView home(String username);

    public FileSystemResource showFile(Integer id, String username);

    public GeneralMessageResponseDto classifyThenUpload(MultipartFile file, String username);

    public GeneralMessageResponseDto deleteFile(Integer id, String username);

    public SyncResponseDto syncToDropbox(String username);

    public List<FolderShowDto> showRoot(String username);

    public FolderShowDto showFolder(Integer id, String username);

}
