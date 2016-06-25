package com.docudile.app.data.dao;

import com.docudile.app.data.entities.Document;
import com.docudile.app.data.entities.Index;
import com.docudile.app.data.entities.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by franc on 5/2/2016.
 */
public interface IndexDao extends GenericDao<Index> {

    public Index createWithReturn(Index index);

    public Index show(String keyword, User user);

}
