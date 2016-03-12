package com.docudile.app.services.impl;

import com.docudile.app.data.dao.UserDao;
import com.docudile.app.data.dto.FolderShowDto;
import com.docudile.app.data.dto.GeneralMessageResponseDto;
import com.docudile.app.data.entities.User;
import com.docudile.app.services.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by FrancAnthony on 3/2/2016.
 */
@Service("documentService")
@Transactional
@PropertySource({"classpath:/storage.properties"})
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private Environment environment;

    @Autowired
    private FileSystemService fileSystemService;

    @Autowired
    private DocumentStructureClassificationService docStructureClassification;

    @Autowired
    private DocxService docxService;

    @Autowired
    private AspriseOCRService aspriseOCRService;

    @Autowired
    private UserDao userDao;

    @Override
    public FileSystemResource showFile(Integer id, String username) {
        FileSystemResource fileSystemResource = fileSystemService.download(id, userDao.show(username));
        return fileSystemResource;
    }

    @Override
    public GeneralMessageResponseDto classifyThenUpload(MultipartFile file, String username) {
        GeneralMessageResponseDto responseDto = new GeneralMessageResponseDto();
        User user = userDao.show(username);
        List<String> text = getLines(file);
        if (text != null) {
            Map<Integer, String> tags = docStructureClassification.tag(text);
            String type = docStructureClassification.classify(new ArrayList<>(tags.values()));
            String path;
            String year = "";
            String toFrom = "";

            System.out.println("Tags: " + tags);
            System.out.println("Type: " + type);
            for (Integer key : tags.keySet()) {
                String curr = tags.get(key);
                if (curr.equals("DATE") || curr.equals("TO_DATE")) {
                    year = getYear(text.get(key));
                }
                if (type.equals("MEMO")) {
                    if (StringUtils.isEmpty(toFrom)) {
                        if (curr.equals("TO") || curr.equals("TO_DATE") && fromHome) {
                            toFrom = getToMemo(text.get(key));
                        }
                        if (!fromHome) {
                            if (curr.equals("FROM")) {
                                toFrom = getFromMemo(text.get(key));
                            }
                            System.out.println("Here Outside");
                            if (curr.equals("OFFICE")) {
                                System.out.println("Here in Office!" + text.get(key));
                                toFrom = text.get(key);
                            }
                        }
                    }
                } else if (type.equals("LETTER")) {
                    if (tags.get(key).equals("SALUTATION")) {
                        toFrom = getToLetter(text.get(key));
                    }
                }
            }
            System.out.println("fromHome: " + fromHome);
            System.out.println("to: " + toFrom);
            if (StringUtils.isNotEmpty(year)) {
                path = year;
            } else {
                path = "uncategorized";
            }
            if (StringUtils.isNotEmpty(type)) {
                path += "/" + type;
                if (type.equals("MEMO")) {
                    if (StringUtils.isNotEmpty(toFrom)) {
                        if (fromHome) {
                            path += "/to/" + toFrom;
                        } else if (!fromHome) {
                            path += "/from/" + toFrom;
                        }
                    } else {
                        path += "/others";
                    }
                } else {
                    if (StringUtils.isNotEmpty(toFrom)) {
                        if (!fromHome) {
                            path += "/from/" + toFrom;
                        } else {
                            path += "/to/" + toFrom;
                        }
                    } else {
                        path += "/others";
                    }
                }
            } else {
                path += "/uncategorized";
            }
            System.out.println("Save Path: " + path);
            fileSystemService.storeFile(file, path, userDao.show(username));
            responseDto.setMessage("file_upload_success");
        } else {
            responseDto.setMessage("error_reading_file");
        }
        return responseDto;
    }

    @Override
    public GeneralMessageResponseDto deleteFile(Integer id, String username) {
        GeneralMessageResponseDto responseDto = new GeneralMessageResponseDto();
        if (fileSystemService.delete(id, userDao.show(username))) {
            responseDto.setMessage("file_deleted_successfully");
        }
        responseDto.setMessage("file_deletion_failed");
        return responseDto;
    }

    @Override
    public List<FolderShowDto> showRoot(String username) {
        return fileSystemService.getRootFolders(userDao.show(username));
    }

    @Override
    public FolderShowDto showFolder(Integer id, String username) {
        return fileSystemService.getFolder(id, userDao.show(username));
    }

    private File multipartToFile(MultipartFile mFile) {
        File file = new File(mFile.getOriginalFilename());
        try {
            mFile.transferTo(file);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> getLines(MultipartFile mfile) {
        List<String> text = null;
        String extension = FilenameUtils.getExtension(mfile.getOriginalFilename());
        if (extension.equals(".docx")) {
            text = docxService.readDocx(multipartToFile(mfile));
        } else try {
            ImageIO.setUseCache(false);
            ImageIO.read(mfile.getInputStream()).toString();
            text = aspriseOCRService.doOCR(multipartToFile(mfile));
        } catch (IOException e) {
        }
        return text;
    }

    private String getYear(String line) {
        Pattern pattern = Pattern.compile("\\d{4}");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    private String getToMemo(String line) {
        Pattern pattern = Pattern.compile("(?i)To\\s*\\:\\s*(.+)(\\s+(Date\\s*\\:\\s*)?(((January|February|March|April|May|June|July|August|September|October|November|December)\\s+(\\d{1,2}),\\s+(\\d{4}))|([0-9]{2}\\/[0-9]{2}\\/[0-9]{2,4})))?");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return null;
    }

    private String getFromMemo(String line) {
        Pattern pattern = Pattern.compile("(?i)From\\s*\\:\\s*(.+)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String getToLetter(String line) {
        Pattern pattern = Pattern.compile("(?i)Dear\\s+(.+)[\\:\\,]");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}
