package com.docudile.app.data.dao.impl;

import com.docudile.app.data.dao.UserDao;
import com.docudile.app.data.entities.User;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by franc on 2/7/2016.
 */
@Repository("userDao")
@Transactional
public class UserDaoImpl extends GenericDaoImpl<User> implements UserDao {

    public User show(String email) {
        Query query = getCurrentSession().createQuery("from User u where u.email = :email");
        query.setParameter("email", email);
        return (User) query.uniqueResult();
    }

}
