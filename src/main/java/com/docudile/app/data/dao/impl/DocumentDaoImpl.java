package com.docudile.app.data.dao.impl;

import com.docudile.app.data.dao.DocumentDao;
import com.docudile.app.data.entities.Document;
import com.docudile.app.data.entities.User;
import com.google.common.base.Preconditions;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by franc on 2/10/2016.
 */
@Repository("fileDao")
@Transactional
public class DocumentDaoImpl extends GenericDaoImpl<Document> implements DocumentDao {

    @Override
    public List<Document> getUserFiles(User user) {
        Query query = getCurrentSession().createQuery("from Document d where d.user = :user");
        query.setParameter("user", user);
        return query.list();
    }

    @Override
    public Document createWithReturn(Document document) {
        try {
            getCurrentSession().saveOrUpdate(Preconditions.checkNotNull(document));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return document;
    }

    @Override
    public List<Document> query(List<String> keywords, User user) {
        Query query = getCurrentSession().createQuery("select d from Index i inner join i.documents d where i.keyword in :keywords and d.user = :user group by d having count(i.keyword) = :size");
        query.setParameterList("keywords", keywords);
        query.setParameter("user", user);
        query.setParameter("size", new Long(keywords.size()));
        return query.list();
    }

    @Override
    public Long getDocumentCount(User user) {
        Query query = getCurrentSession().createQuery("select count(*) from Document d where d.user = :user");
        query.setParameter("user", user);
        return (long) query.uniqueResult();
    }

    @Override
    public Long getTotalSize(User user) {
        Query query = getCurrentSession().createQuery("select sum(d.size) from Document d where d.user = :user");
        query.setParameter("user", user);
        Object result = query.uniqueResult();
        if (result != null) {
            return (long) query.uniqueResult();
        }
        return 0L;
    }

}
