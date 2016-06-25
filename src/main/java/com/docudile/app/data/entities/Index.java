package com.docudile.app.data.entities;

import javax.persistence.*;
import java.util.List;

/**
 * Created by franc on 4/29/2016.
 */
@Entity
@Table(name = "indices")
public class Index {

    @Id @GeneratedValue
    @Column(name = "index_id")
    private Integer id;

    @Column(name = "keyword")
    private String keyword;

    @ManyToMany(mappedBy = "indices", fetch = FetchType.LAZY)
    private List<Document> documents;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

}
