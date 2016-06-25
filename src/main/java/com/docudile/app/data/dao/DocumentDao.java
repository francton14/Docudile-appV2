package com.docudile.app.data.dao;

import com.docudile.app.data.entities.Document;
import com.docudile.app.data.entities.User;

import java.util.List;

/**
 * Created by franc on 2/10/2016.
 */
public interface DocumentDao extends GenericDao<Document> {

    public List<Document> getUserFiles(User user);

    public Document createWithReturn(Document document);

    public List<Document> query(List<String> keywords, User user);

    public Long getDocumentCount(User user);

    public Long getTotalSize(User user);

}
