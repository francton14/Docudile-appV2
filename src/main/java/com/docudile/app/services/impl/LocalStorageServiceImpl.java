package com.docudile.app.services.impl;

import com.docudile.app.services.LocalStorageService;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.*;

/**
 * Created by franc on 3/11/2016.
 */
@Service("localStorageService")
@Transactional
public class LocalStorageServiceImpl implements LocalStorageService {

    @Override
    public boolean upload(String path, InputStream file) {
        File filepath = new File(path);
        if (!filepath.isDirectory()) {
            filepath.getParentFile().mkdirs();
        }
        try {
            OutputStream outputStream = new FileOutputStream(filepath);
            FileCopyUtils.copy(file, outputStream);
            outputStream.close();
            file.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public FileSystemResource getFile(String path) {
        return new FileSystemResource(path);
    }

    @Override
    public boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }

}
