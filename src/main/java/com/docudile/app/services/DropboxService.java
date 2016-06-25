package com.docudile.app.services;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by franc on 2/7/2016.
 */
public interface DropboxService {

    public String linkDropbox();

    public String finishAuth(Map<String, String[]> parameters);

    public List<String> index(String accessToken);

    public boolean createFolder(String path, String accessToken);

    public boolean uploadFile(String path, InputStream file, String accessToken);

    public String getFile(String path, String accessToken);

    public boolean deleteFile(String path, String accessToken);

    public Long getUsedSpace(String accessToken);

    public Long getTotalSpace(String accessToken);

    public String name(String accessToken);

    public String getEmail(String accessToken);

    public Boolean isEmailVerified(String accessToken);

    public String accountType(String accessToken);

    public Boolean isPaired(String accessToken);

    public String teamName(String accessToken);

}
