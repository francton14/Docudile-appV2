package com.docudile.app.services.impl;

import com.docudile.app.data.dao.DocumentDao;
import com.docudile.app.data.dao.FolderDao;
import com.docudile.app.data.dao.IndexDao;
import com.docudile.app.data.dao.UserDao;
import com.docudile.app.data.dto.*;
import com.docudile.app.data.entities.*;
import com.docudile.app.data.entities.Document;
import com.docudile.app.exceptions.InvalidRequestException;
import com.docudile.app.services.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by franc on 2/10/2016.
 */
@Service("fileSystemService")
@Transactional
@PropertySource({"classpath:/storage.properties"})
public class StorageServiceImpl implements StorageService {

    private static final Logger logger = LoggerFactory.getLogger(StorageServiceImpl.class);

    @Autowired
    private Environment environment;

    @Autowired
    private FolderDao folderDao;

    @Autowired
    private DocumentDao documentDao;

    @Autowired
    private IndexDao indexDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DocumentStructureClassificationService docStructureClassification;

    @Autowired
    private LocalStorageService localStorageService;

    @Autowired
    private StringProcessorService stringProcessorService;

    @Autowired
    private DocxService docxService;

    @Autowired
    private OpticalRecognitionService opticalRecognitionService;

    @Autowired
    private DropboxService dropboxService;

    @Override
    public ModelAndView accessStorage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView mav = new ModelAndView();
        if (authentication instanceof AnonymousAuthenticationToken) {
            mav.setViewName("index");
        } else {
            mav.setViewName("storage");
            logger.debug("Current User: " + authentication.getName());
            mav.addObject("user", userDao.show(authentication.getName()));
            mav.addObject("sideNav", "storage");
        }
        return mav;
    }

    @Override
    public ModelAndView accessUpload(String username) {
        ModelAndView mav = new ModelAndView("upload");
        User user = userDao.show(username);
        mav.addObject("user", user);
        mav.addObject("sideNav", "upload");
        return mav;
    }

    @Override
    public ModelAndView accessSearch(String username) {
        ModelAndView mav = new ModelAndView("search");
        User user = userDao.show(username);
        mav.addObject("user", user);
        mav.addObject("sideNav", "search");
        return mav;
    }

    @Override
    public GeneralMessageResponse storeFile(MultipartFile mfile, String username) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        threadMXBean.setThreadContentionMonitoringEnabled(true);
        long userTimeStart = threadMXBean.getCurrentThreadUserTime();
        long cpuTimeStart = threadMXBean.getCurrentThreadCpuTime();
        User user = userDao.show(username);
        GeneralMessageResponse response = new GeneralMessageResponse();
        List<String> lines = cleanseLines(getLines(mfile));
        List<Index> indices = getIndices(getTokens(lines), user);
        String path = generatePath(lines, user);
        Folder folder = createFoldersFromPath(path, user);
        String filename = mfile.getOriginalFilename();
        Document document = new Document();
        document.setFilename(filename);
        document.setUser(user);
        document.setFolder(folder);
        document.setDateUploaded(new Date());
        document.setSize(mfile.getSize());
        document.setIndices(indices);
        document = documentDao.createWithReturn(document);
        if (document.getId() != null) {
            String filepath = environment.getProperty("storage.users") + user.getId() + "/files/" + path + "/" + filename;
            try {
                localStorageService.upload(filepath, mfile.getInputStream());
                response.setMessage(path + "/" + filename);
                long userTime = threadMXBean.getCurrentThreadUserTime() - userTimeStart;
                long cpuTime = threadMXBean.getCurrentThreadCpuTime() - cpuTimeStart;
                long systemTime = cpuTime - userTime;
                logger.debug("Finished: User Time: " + userTime + " CPU Time: " + cpuTime + " System Time: " + systemTime);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long userTime = threadMXBean.getCurrentThreadUserTime() - userTimeStart;
        long cpuTime = threadMXBean.getCurrentThreadCpuTime() - cpuTimeStart;
        long systemTime = cpuTime - userTime;
        logger.debug("Finished: User Time: " + userTime + " CPU Time: " + cpuTime + " System Time: " + systemTime);
        response.setMessage("file_not_uploaded");
        return response;
    }

    @Override
    public List<FolderShow> getRootFolders(String username) {
        User user = userDao.show(username);
        List<FolderShow> folders = new ArrayList<FolderShow>();
        for (Folder folder : folderDao.root(user.getId())) {
            folders.add(convertToDto(folder));
        }
        return folders;
    }

    @Override
    public FolderShow getFolder(Integer id, String username) {
        User user = userDao.show(username);
        Folder folder = folderDao.show(id);
        if (folder.getUser().getId().equals(user.getId())) {
            return convertToDto(folder);
        }
        return null;
    }

    public FileSystemResource download(Integer id, String username) {
        User user = userDao.show(username);
        Document document = documentDao.show(id);
        if (document.getUser().getId().equals(user.getId())) {
            String filepath = environment.getProperty("storage.users") + user.getId() + "/files/" + getPath(document.getFolder()) + "/" + document.getFilename();
            return localStorageService.getFile(filepath);
        }
        return null;
    }

    @Override
    public GeneralMessageResponse delete(Integer id, String username) {
        User user = userDao.show(username);
        Document document = documentDao.show(id);
        GeneralMessageResponse response = new GeneralMessageResponse();
        if (document.getUser().getId().equals(user.getId())) {
            String filepath = environment.getProperty("storage.users") + user.getId() + "/files/" + getPath(document.getFolder()) + "/" + document.getFilename();
            if (localStorageService.deleteFile(filepath)) {
                response.setMessage("file_deleted_successfully");
                return response;
            }
        }
        response.setMessage("file_deletion_failed");
        return response;
    }

    @Override
    public SyncResponse syncDropbox(String username) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        threadMXBean.setThreadContentionMonitoringEnabled(true);
        long userTimeStart = threadMXBean.getCurrentThreadUserTime();
        long cpuTimeStart = threadMXBean.getCurrentThreadCpuTime();
        SyncResponse response = new SyncResponse();
        User user = userDao.show(username);
        List<String> failed = new ArrayList<>();
        List<String> localFiles = new ArrayList<>();
        List<String> dropboxFiles = dropboxService.index(user.getDropboxAccessToken());
        for (Document document : documentDao.getUserFiles(user)) {
            localFiles.add("/" + getPath(document.getFolder()) + document.getFilename());
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
            java.io.File file = new java.io.File(environment.getProperty("storage.users") + user.getId() + "/files" + localFile);
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
        response.setFailed(failed);
        if (failed.size() > 0) {
            response.setMessage("some_document_failed");
        } else {
            response.setMessage("all_documents_synced");
        }
        long userTime = threadMXBean.getCurrentThreadUserTime() - userTimeStart;
        long cpuTime = threadMXBean.getCurrentThreadCpuTime() - cpuTimeStart;
        long systemTime = cpuTime - userTime;
        logger.debug("Finished: User Time: " + userTime + " CPU Time: " + cpuTime + " System Time: " + systemTime);
        return response;
    }

    @Override
    public ResponseEntity<?> search(Search search, BindingResult result, String username) {
        User user = userDao.show(username);
        if (!result.hasErrors()) {
            List<String> tokens = stringProcessorService.process(search.getQuery());
            List<FileShow> searchResult = new ArrayList<>();
            List<Document> documents = documentDao.query(tokens, user);
            for (Document document : documents) {
                searchResult.add(convertToDto(document));
            }
            return new ResponseEntity<>(searchResult, HttpStatus.OK);
        }
        throw new InvalidRequestException("Invalid Request.", result);
    }

    private String generatePath(List<String> lines, User user) {
        String path = "Missing Info or Unclassified";
        if (lines != null) {
            Map<Integer, String> labels = docStructureClassification.labelParts(lines);
            String type = docStructureClassification.classify(new ArrayList<>(labels.values()));
            boolean fromHome = isFromHome(labels, lines, user);
            Map<String, String> info = extractInfo(labels, lines, fromHome, type);
            if (info.containsKey("DATE") && info.containsKey("TO_FROM") && StringUtils.isNotEmpty(type)) {
                path = info.get("DATE") + "/" + type;
                if (fromHome) {
                    path += "/to/" + info.get("TO_FROM");
                } else if (!fromHome) {
                    path += "/from/" + info.get("TO_FROM");
                }
            }
        }
        return path;
    }

    private Map<String, String> extractInfo(Map<Integer, String> labels, List<String> lines, boolean fromHome, String type) {
        Map<String, String> info = new HashMap<>();
        for (Integer key : labels.keySet()) {
            String curr = labels.get(key);
            if (curr.equals("DATE") || curr.equals("TO_DATE")) {
                info.put("DATE", getYear(lines.get(key)));
            }
            if (type.equals("MEMO")) {
                if (!info.containsKey("TO_FROM")) {
                    if (curr.equals("TO") || curr.equals("TO_DATE") && fromHome) {
                        info.put("TO_FROM", getToMemo(lines.get(key)));
                    }
                    if (!fromHome) {
                        if (curr.equals("FROM")) {
                            info.put("TO_FROM", getFromMemo(lines.get(key)));
                        }
                        if (curr.equals("OFFICE")) {
                            info.put("TO_FROM", lines.get(key));
                        }
                    }
                }
            } else if (type.equals("LETTER")) {
                if (labels.get(key).equals("SALUTATION")) {
                    info.put("TO_FROM", getToLetter(lines.get(key)));
                }
            }
        }
        return info;
    }

    private boolean isFromHome(Map<Integer, String> labels, List<String> lines, User user) {
        for (Integer key : labels.keySet()) {
            if (labels.get(key).equals("OFFICE")) {
                if (StringUtils.getLevenshteinDistance(lines.get(key), user.getOrganization()) <= 5) {
                    return true;
                }
            }
        }
        return false;
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
            text = opticalRecognitionService.extract(multipartToFile(mfile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    private List<String> cleanseLines(List<String> lines) {
        List<String> cleansedLines = new ArrayList<>();
        for (String line : lines) {
            line = line.replaceAll("[^\\p{L}\\p{Nd}\\s\\:]+", "");
            if (StringUtils.isNotBlank(line)) {
                cleansedLines.add(line);
            }
        }
        return cleansedLines;
    }

    private Set<String> getTokens(List<String> lines) {
        Set<String> tokens = new TreeSet<>();
        for (String line : lines) {
            for (String tempToken : stringProcessorService.process(line)) {
                tokens.add(tempToken);
            }
        }
        return tokens;
    }

    private List<Index> getIndices(Set<String> tokens, User user) {
        List<Index> indices = new ArrayList<>();
        for (String token : tokens) {
            Index index = new Index();
            index.setKeyword(token);
            Index temp = indexDao.show(index.getKeyword(), user);
            if (temp != null) {
                index = temp;
            } else {
                index = indexDao.createWithReturn(index);
            }
            indices.add(index);
        }
        return indices;
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

    private Folder createFoldersFromPath(String path, User user) {
        return createFoldersFromPath(null, new LinkedList<>(Arrays.asList(path.split("/"))), user);
    }

    private Folder createFoldersFromPath(Folder base, LinkedList<String> path, User user) {
        if (!path.isEmpty()) {
            String folderName = path.removeFirst();
            if (base == null) {
                base = folderDao.show(folderName, user);
                if (base == null) {
                    Folder temp = new Folder();
                    temp.setUser(user);
                    temp.setName(folderName);
                    base = folderDao.createWithReturn(temp);
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
                temp = folderDao.createWithReturn(temp);
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

    private FolderShow convertToDto(Folder folder) {
        FolderShow dto = new FolderShow();
        dto.setId(folder.getId());
        dto.setName(folder.getName());
        if (folder.getParentFolder() != null) {
            dto.setParentFolder(folder.getParentFolder().getName());
        }
        List<FolderShow> childFolders = new ArrayList<FolderShow>();
        for (Folder childFolder : folder.getChildFolders()) {
            childFolders.add(convertToDto(childFolder));
        }
        dto.setChildFolders(childFolders);
        List<FileShow> files = new ArrayList<FileShow>();
        for (Document document : folder.getDocuments()) {
            files.add(convertToDto(document));
        }
        dto.setFiles(files);
        dto.setDateModified(findLatestDate(folder));
        dto.setPath(getPath(folder));
        return dto;
    }

    private FileShow convertToDto(Document document) {
        FileShow dto = new FileShow();
        dto.setId(document.getId());
        dto.setFilename(document.getFilename());
        dto.setPath(getPath(document.getFolder()));
        dto.setDateUploaded(document.getDateUploaded());
        return dto;
    }

    private Date findLatestDate(Folder folder) {
        return findLatestDate(folder, null);
    }

    private Date findLatestDate(Folder folder, Date date) {
        for (Document document : folder.getDocuments()) {
            Date currDate = document.getDateUploaded();
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

}
