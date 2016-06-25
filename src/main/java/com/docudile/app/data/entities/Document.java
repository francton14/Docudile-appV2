package com.docudile.app.data.entities;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by franc on 2/10/2016.
 */
@Entity
@Table(name = "documents", uniqueConstraints = @UniqueConstraint(columnNames = {"folder_id", "filename", "user_id"}))
public class Document {

    @Id @GeneratedValue
    @Column(name = "document_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @Column(name = "filename")
    private String filename;

    @Column(name = "date_uploaded")
    private Date dateUploaded;

    @Column(name = "size")
    private Long size;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "document_index", joinColumns = {@JoinColumn(name = "document_id")}, inverseJoinColumns = {@JoinColumn(name = "index_id")})
    List<Index> indices;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Date getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public List<Index> getIndices() {
        return indices;
    }

    public void setIndices(List<Index> indices) {
        this.indices = indices;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
