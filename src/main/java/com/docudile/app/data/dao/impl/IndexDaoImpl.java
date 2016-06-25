package com.docudile.app.data.dao.impl;

import com.docudile.app.data.dao.IndexDao;
import com.docudile.app.data.entities.Document;
import com.docudile.app.data.entities.Index;
import com.docudile.app.data.entities.User;
import com.google.common.base.Preconditions;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by franc on 5/2/2016.
 */
@Repository("indexDao")
@Transactional
public class IndexDaoImpl extends GenericDaoImpl<Index> implements IndexDao {

    @Override
    public Index createWithReturn(Index index) {
        try {
            getCurrentSession().save(Preconditions.checkNotNull(index));
            return index;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public Index show(String keyword, User user) {
        Query query = getCurrentSession().createQuery("select i from Index i inner join i.documents d where i.keyword = :keyword and d.user = :user");
        query.setParameter("keyword", keyword);
        query.setParameter("user", user);
        return (Index) query.uniqueResult();
    }

}
