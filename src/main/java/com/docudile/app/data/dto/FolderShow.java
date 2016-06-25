package com.docudile.app.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by franc on 2/11/2016.
 */
public class FolderShow implements Serializable {

    @JsonProperty
    private Integer id;

    @JsonProperty
    private String name;

    @JsonProperty
    private String parentFolder;

    @JsonProperty
    private List<FolderShow> childFolders;

    @JsonProperty
    private List<FileShow> files;

    @JsonProperty
    private String path;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a", timezone = "Asia/Shanghai")
    private Date dateModified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(String parentFolder) {
        this.parentFolder = parentFolder;
    }

    public List<FolderShow> getChildFolders() {
        return childFolders;
    }

    public void setChildFolders(List<FolderShow> childFolders) {
        this.childFolders = childFolders;
    }

    public List<FileShow> getFiles() {
        return files;
    }

    public void setFiles(List<FileShow> files) {
        this.files = files;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

}
