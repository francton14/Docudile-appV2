package com.docudile.app.data.dao.impl;

import com.docudile.app.data.dao.FileDao;
import com.docudile.app.data.dao.FolderDao;
import com.docudile.app.data.entities.File;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by franc on 2/10/2016.
 */
@Repository("fileDao")
@Transactional
public class FileDaoImpl extends GenericDaoImpl<File> implements FileDao {

    @Override
    public List<File> getUserFiles(Integer userId) {
        Query query = getCurrentSession().createQuery("from File f where f.user.id = :userId");
        query.setParameter("userId", userId);
        return query.list();
    }

}
