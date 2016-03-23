package com.docudile.app.services.impl;

import com.docudile.app.data.dao.FileDao;
import com.docudile.app.data.dao.FolderDao;
import com.docudile.app.data.dao.UserDao;
import com.docudile.app.data.dto.FileShowDto;
import com.docudile.app.data.dto.FolderShowDto;
import com.docudile.app.data.entities.*;
import com.docudile.app.data.entities.File;
import com.docudile.app.services.DropboxService;
import com.docudile.app.services.FileSystemService;
import com.docudile.app.services.LocalStorageService;
import com.docudile.app.services.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by franc on 2/10/2016.
 */
@Service("fileSystemService")
@Transactional
@PropertySource({"classpath:/storage.properties"})
public class FileSystemServiceImpl implements FileSystemService {

    @Autowired
    private Environment environment;

    @Autowired
    private FolderDao folderDao;

    @Autowired
    private FileDao fileDao;

    @Autowired
    private LocalStorageService localStorageService;

    @Autowired
    private DropboxService dropboxService;

    @Autowired
    private UserService userService;

    @Override
    public boolean storeFile(MultipartFile mfile, String path, User user) {
        Folder folder = createFoldersFromPath(path, user);
        String filename = mfile.getOriginalFilename();
        String filepath = environment.getProperty("storage.users") + user.getUsername() + "/files/" + path + "/" + filename;
        System.err.println("File path: " + filepath);
        File file = new File();
        file.setFilename(filename);
        file.setUser(user);
        file.setFolder(folder);
        file.setDateUploaded(convertDateToString(new Date()));
        if (fileDao.update(file)) {
            try {
                return localStorageService.upload(filepath, mfile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public List<FolderShowDto> getRootFolders(User user) {
        List<FolderShowDto> folders = new ArrayList<FolderShowDto>();
        for (Folder folder : folderDao.root(user.getId())) {
            folders.add(convertToDto(folder));
        }
        return folders;
    }

    @Override
    public FolderShowDto getFolder(Integer id, User user) {
        Folder folder = folderDao.show(id);
        if (folder.getUser().getId().equals(user.getId())) {
            return convertToDto(folder);
        }
        return null;
    }

    public FileSystemResource download(Integer id, User user) {
        File file = fileDao.show(id);
        if (file.getUser().getId().equals(user.getId())) {
            String filepath = environment.getProperty("storage.users") + user.getUsername() + "/files/" + getPath(file.getFolder()) + "/" + file.getFilename();
            return localStorageService.getFile(filepath);
        }
        return null;
    }

    @Override
    public boolean delete(Integer id, User user) {
        File file = fileDao.show(id);
        if (file.getUser().getId().equals(user.getId())) {
            String filepath = environment.getProperty("storage.users") + user.getUsername() + "/files/" + getPath(file.getFolder()) + "/" + file.getFilename();
            return localStorageService.deleteFile(filepath);
        }
        return false;
    }

    @Override
    public List<String> syncDropbox(User user) {
        List<String> failed = new ArrayList<>();
        List<String> localFiles = new ArrayList<>();
        List<String> dropboxFiles = dropboxService.index(user.getDropboxAccessToken());
        for (File file : fileDao.getUserFiles(user.getId())) {
            localFiles.add("/" + getPath(file.getFolder()) + file.getFilename());
        }
        List<String> deletables = new ArrayList<>(dropboxFiles);
        deletables.removeAll(localFiles);
        dropboxFiles.removeAll(deletables);
        for (String deletable : deletables) {
            dropboxService.deleteFile(deletable, user.getDropboxAccessToken());
        }
        localFiles.removeAll(dropboxFiles);
        for (String localFile : localFiles) {
            boolean failedFile = false;
            java.io.File file = new java.io.File(environment.getProperty("storage.users") + user.getUsername() + "/files" + localFile);
            try {
                InputStream fileInputStream = new FileInputStream(file);
                failedFile = dropboxService.uploadFile(localFile, fileInputStream, user.getDropboxAccessToken());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (!failedFile) {
                failed.add(localFile);
            }
        }
        return failed;
    }

    private Folder createFoldersFromPath(String path, User user) {
        return createFoldersFromPath(null, new LinkedList<>(Arrays.asList(path.split("/"))), user);
    }

    private Folder createFoldersFromPath(Folder base, LinkedList<String> path, User user) {
        if (!path.isEmpty()) {
            String folderName = path.removeFirst();
            if (base == null) {
                base = folderDao.show(folderName, user.getUsername());
                if (base == null) {
                    Folder temp = new Folder();
                    temp.setUser(user);
                    temp.setName(folderName);
                    base = folderDao.createReturnFolder(temp);
                }
                return createFoldersFromPath(base, path, user);
            } else {
                if (base.getChildFolders() != null) {
                    for (Folder childFolder : base.getChildFolders()) {
                        if (childFolder.getName().equals(folderName)) {
                            return createFoldersFromPath(childFolder, path, user);
                        }
                    }
                }
                Folder temp = new Folder();
                temp.setUser(user);
                temp.setName(folderName);
                temp.setParentFolder(base);
                temp = folderDao.createReturnFolder(temp);
                return createFoldersFromPath(temp, path, user);
            }
        }
        return base;
    }

    private String getPath(Folder folder) {
        return getPath(folder, "");
    }

    private String getPath(Folder folder, String path) {
        if (folder.getParentFolder() != null) {
            return getPath(folder.getParentFolder(), folder.getName() + "/" + path);
        }
        return folder.getName() + "/" + path;
    }

    private FolderShowDto convertToDto(Folder folder) {
        FolderShowDto dto = new FolderShowDto();
        dto.setId(folder.getId());
        dto.setName(folder.getName());
        if (folder.getParentFolder() != null) {
            dto.setParentFolder(folder.getParentFolder().getName());
        }
        dto.setUser(userService.convert(folder.getUser()));
        List<FolderShowDto> childFolders = new ArrayList<FolderShowDto>();
        for (Folder childFolder : folder.getChildFolders()) {
            childFolders.add(convertToDto(childFolder));
        }
        dto.setChildFolders(childFolders);
        List<FileShowDto> files = new ArrayList<FileShowDto>();
        for (File file : folder.getFiles()) {
            files.add(convertToDto(file));
        }
        dto.setFiles(files);
        dto.setDateModified(convertDateToString(findLatestDate(folder)));
        dto.setPath(getPath(folder));
        return dto;
    }

    private FileShowDto convertToDto(File file) {
        FileShowDto dto = new FileShowDto();
        dto.setId(file.getId());
        dto.setFilename(file.getFilename());
        dto.setPath(getPath(file.getFolder()));
        dto.setDateUploaded(file.getDateUploaded());
        dto.setUser(userService.convert(file.getUser()));
        return dto;
    }

    private Date findLatestDate(Folder folder) {
        return findLatestDate(folder, null);
    }

    private Date findLatestDate(Folder folder, Date date) {
        for (File file : folder.getFiles()) {
            Date currDate = convertStringToDate(file.getDateUploaded());
            if (currDate != null && date != null) {
                if (currDate.after(date)) {
                    date = currDate;
                }
            }
            if (date == null) {
                date = currDate;
            }
        }
        for (Folder childFolder : folder.getChildFolders()) {
            date = findLatestDate(childFolder, date);
        }
        return date;
    }

    private Date convertStringToDate(String date) {
        if (StringUtils.isNotEmpty(date)) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy, E");
            formatter.setLenient(false);
            try {
                return formatter.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String convertDateToString(Date date) {
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy, E");
            return formatter.format(date);
        }
        return "Not Yet Modified";
    }

}
