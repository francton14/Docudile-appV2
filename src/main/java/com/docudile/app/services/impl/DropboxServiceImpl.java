package com.docudile.app.services.impl;

import com.docudile.app.services.DropboxService;
import com.dropbox.core.*;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.DbxFiles;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by franc on 2/7/2016.
 */
@Service("dropboxService")
@Transactional
@PropertySource({"classpath:/dropbox.properties"})
public class DropboxServiceImpl implements DropboxService {

    @Autowired
    private Environment environment;

    public String linkDropbox() {
        DbxAppInfo appInfo = new DbxAppInfo(environment.getProperty("dropbox.appkey"), environment.getProperty("dropbox.appsecret"));
        DbxRequestConfig config = new DbxRequestConfig(environment.getProperty("dropbox.appname"), Locale.getDefault().toString());
        HttpSession session = getSession();
        String sessionKey = "dropbox-auth-csrf-token";
        DbxSessionStore sessionStore = new DbxStandardSessionStore(session, sessionKey);
        String redirectUri = "http://localhost:8080/profile/dropbox/auth/finish";
        DbxWebAuth webAuth = new DbxWebAuth(config, appInfo, redirectUri, sessionStore);
        return webAuth.start();
    }

    public String finishAuth(Map<String, String[]> parameters) {
        DbxAppInfo appInfo = new DbxAppInfo(environment.getProperty("dropbox.appkey"), environment.getProperty("dropbox.appsecret"));
        DbxRequestConfig config = new DbxRequestConfig("Docudi.le/1.0", Locale.getDefault().toString());
        HttpSession session = getSession();
        String sessionKey = "dropbox-auth-csrf-token";
        DbxSessionStore sessionStore = new DbxStandardSessionStore(session, sessionKey);
        String redirectUri = "http://localhost:8080/profile/dropbox/auth/finish";
        DbxWebAuth webAuth = new DbxWebAuth(config, appInfo, redirectUri, sessionStore);
        try {
             return webAuth.finish(parameters).accessToken;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<String> index(String accessToken) {
        DbxRequestConfig config = new DbxRequestConfig("Docudi.le/1.0", Locale.getDefault().toString());
        DbxClientV2 client = new DbxClientV2(config, accessToken);
        return getAllFilesFromDropbox(client);
    }

    public List<String> getAllFilesFromDropbox(DbxClientV2 client) {
        return getAllFilesFromDropbox("", client);
    }

    public List<String> getAllFilesFromDropbox(String path, DbxClientV2 client) {
        List<String> filePaths = new ArrayList<>();
        if (StringUtils.isEmpty(path)) {
            path = "";
        }
        try {
            DbxFiles.ListFolderResult listFolderResult = client.files.listFolder(path);
            do {
                for (DbxFiles.Metadata metadata : listFolderResult.entries) {
                    String tempPath = path + "/" + metadata.name;
                    if (metadata instanceof DbxFiles.FileMetadata) {
                        filePaths.add(tempPath);
                    } else if (metadata instanceof DbxFiles.FolderMetadata) {
                        filePaths.addAll(getAllFilesFromDropbox(tempPath, client));
                    }
                }
            } while (listFolderResult.hasMore);
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return filePaths;
    }

    public boolean createFolder(String path, String accessToken) {
        DbxRequestConfig config = new DbxRequestConfig("Docudi.le/1.0", Locale.getDefault().toString());
        DbxClientV2 client = new DbxClientV2(config, accessToken);
        try {
            client.files.createFolder(path);
            return true;
        } catch (DbxException e) {}
        return false;
    }

    public boolean uploadFile(String path, InputStream file, String accessToken) {
        DbxRequestConfig config = new DbxRequestConfig("Docudi.le/1.0", Locale.getDefault().toString());
        DbxClientV2 client = new DbxClientV2(config, accessToken);
        try {
            client.files.uploadBuilder(path).run(file);
            return true;
        } catch (DbxFiles.UploadException e) {
            e.printStackTrace();
        } catch (DbxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getFile(String path, String accessToken) {
        DbxRequestConfig config = new DbxRequestConfig("Docudi.le/1.0", Locale.getDefault().toString());
        DbxClientV2 client = new DbxClientV2(config, accessToken);
        try {
            FileOutputStream out = new FileOutputStream(environment.getProperty("dropbox.downloadstorage"));
            DbxFiles.FileMetadata fileMetadata = client.files.downloadBuilder(path).run(out);
            return environment.getProperty("dropbox.downloadstorage") + "/" + fileMetadata.name;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteFile(String path, String accessToken) {
        DbxRequestConfig config = new DbxRequestConfig("Docudi.le/1.0", Locale.getDefault().toString());
        DbxClientV2 client = new DbxClientV2(config, accessToken);
        try {
            client.files.delete(path);
            return true;
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Long getUsedSpace(String accessToken) {
        DbxRequestConfig config = new DbxRequestConfig("Docudi.le/1.0", Locale.getDefault().toString());
        DbxClientV2 client = new DbxClientV2(config, accessToken);
        try {
            return client.users.getSpaceUsage().used;
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Long getTotalSpace(String accessToken) {
        DbxRequestConfig config = new DbxRequestConfig("Docudi.le/1.0", Locale.getDefault().toString());
        DbxClientV2 client = new DbxClientV2(config, accessToken);
        try {
            return client.users.getSpaceUsage().allocation.getIndividualValue().allocated;
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String name(String accessToken) {
        DbxRequestConfig config = new DbxRequestConfig("Docudi.le/1.0", Locale.getDefault().toString());
        DbxClientV2 client = new DbxClientV2(config, accessToken);
        try {
            return client.users.getCurrentAccount().name.displayName;
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getEmail(String accessToken) {
        DbxRequestConfig config = new DbxRequestConfig("Docudi.le/1.0", Locale.getDefault().toString());
        DbxClientV2 client = new DbxClientV2(config, accessToken);
        try {
            return client.users.getCurrentAccount().email;
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean isEmailVerified(String accessToken) {
        DbxRequestConfig config = new DbxRequestConfig("Docudi.le/1.0", Locale.getDefault().toString());
        DbxClientV2 client = new DbxClientV2(config, accessToken);
        try {
            return client.users.getCurrentAccount().emailVerified;
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String accountType(String accessToken) {
        DbxRequestConfig config = new DbxRequestConfig("Docudi.le/1.0", Locale.getDefault().toString());
        DbxClientV2 client = new DbxClientV2(config, accessToken);
        try {
            return client.users.getCurrentAccount().accountType.getTag().name();
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean isPaired(String accessToken) {
        DbxRequestConfig config = new DbxRequestConfig("Docudi.le/1.0", Locale.getDefault().toString());
        DbxClientV2 client = new DbxClientV2(config, accessToken);
        try {
            return client.users.getCurrentAccount().isPaired;
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String teamName(String accessToken) {
        DbxRequestConfig config = new DbxRequestConfig("Docudi.le/1.0", Locale.getDefault().toString());
        DbxClientV2 client = new DbxClientV2(config, accessToken);
        try {
            return client.users.getCurrentAccount().team.name;
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }

}
