package com.docudile.app.services;

import com.docudile.app.data.dto.FileShowDto;
import com.docudile.app.data.dto.FolderShowDto;
import com.docudile.app.data.entities.Folder;
import com.docudile.app.data.entities.User;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by franc on 2/10/2016.
 */
public interface FileSystemService {

    public boolean storeFile(MultipartFile file, String path, User user);

    public List<FolderShowDto> getRootFolders(User user);

    public FolderShowDto getFolder(Integer id, User user);

    public FileSystemResource download(Integer id, User user);

    public boolean delete(Integer id, User user);

}
