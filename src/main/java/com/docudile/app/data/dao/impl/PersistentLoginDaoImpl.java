package com.docudile.app.data.dao.impl;

import com.docudile.app.data.dao.PersistentLoginDao;
import com.docudile.app.data.dao.UserDao;
import com.docudile.app.data.entities.PersistentLogin;
import com.docudile.app.data.entities.User;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by franc on 5/20/2016.
 */
@Repository("persistentLoginDao")
@Transactional
public class PersistentLoginDaoImpl extends GenericDaoImpl<PersistentLogin> implements PersistentLoginDao, PersistentTokenRepository {

    @Autowired
    private UserDao userDao;

    @Override
    public void createNewToken(PersistentRememberMeToken persistentRememberMeToken) {
        PersistentLogin persistentLogin = new PersistentLogin();
        persistentLogin.setSeries(persistentRememberMeToken.getSeries());
        persistentLogin.setUser(userDao.show(persistentRememberMeToken.getUsername()));
        persistentLogin.setToken(persistentRememberMeToken.getTokenValue());
        persistentLogin.setLastUsed(persistentRememberMeToken.getDate());
        this.create(persistentLogin);
    }

    @Override
    public void updateToken(String series, String token, Date lastUsed) {
        PersistentLogin persistentLogin = this.show(series);
        persistentLogin.setSeries(series);
        persistentLogin.setToken(token);
        persistentLogin.setLastUsed(lastUsed);
        this.update(persistentLogin);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String series) {
        try {
            PersistentLogin persistentLogin = this.show(series);
            return new PersistentRememberMeToken(persistentLogin.getUser().getEmail(), persistentLogin.getSeries(), persistentLogin.getToken(), persistentLogin.getLastUsed());
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void removeUserTokens(String email) {
        User user = userDao.show(email);
        PersistentLogin persistentLogin = this.show(user);
        this.delete(persistentLogin);
    }

    @Override
    public PersistentLogin show(String series) {
        Query query = getCurrentSession().createQuery("from PersistentLogin pl where pl.series = :series");
        query.setParameter("series", series);
        return (PersistentLogin) query.uniqueResult();
    }

    @Override
    public PersistentLogin show(User user) {
        Query query = getCurrentSession().createQuery("from PersistentLogin pl where pl.user = :user");
        query.setParameter("user", user);
        return (PersistentLogin) query.uniqueResult();
    }

}
