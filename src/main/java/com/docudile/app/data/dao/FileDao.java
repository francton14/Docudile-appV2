package com.docudile.app.data.dao;

import com.docudile.app.data.entities.File;

import java.util.List;

/**
 * Created by franc on 2/10/2016.
 */
public interface FileDao extends GenericDao<File> {

    public List<File> getUserFiles(Integer userId);

}
