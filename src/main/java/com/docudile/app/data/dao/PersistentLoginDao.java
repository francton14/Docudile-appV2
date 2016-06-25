package com.docudile.app.data.dao;

import com.docudile.app.data.entities.PersistentLogin;
import com.docudile.app.data.entities.User;

/**
 * Created by franc on 5/20/2016.
 */
public interface PersistentLoginDao extends GenericDao<PersistentLogin> {

    public PersistentLogin show(String series);

    public PersistentLogin show(User user);

}
