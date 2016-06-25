package com.docudile.app.data.dto;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by franc on 5/27/2016.
 */
public class Search {

    @NotEmpty
    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

}
