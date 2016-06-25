package com.docudile.app.data.dao;

import com.docudile.app.data.entities.Folder;
import com.docudile.app.data.entities.User;

import java.util.List;
import java.util.Set;

/**
 * Created by franc on 2/10/2016.
 */
public interface FolderDao extends GenericDao<Folder> {

    public Folder show(String name, User user);

    public Folder show(Integer id);

    public Folder show(String name, Integer parentId);

    public Folder createWithReturn(Folder folder);

    public List<Folder> root(Integer userId);
}
