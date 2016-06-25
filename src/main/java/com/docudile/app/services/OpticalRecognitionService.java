package com.docudile.app.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * Created by franc on 5/4/2016.
 */
public interface OpticalRecognitionService {

    public List<String> extract(File file);

}
