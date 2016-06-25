package com.docudile.app.services;

import java.util.List;

/**
 * Created by franc on 5/3/2016.
 */
public interface StringProcessorService {

    public List<String> process(String line);

    public List<String> getSynonyms(String word);

}
