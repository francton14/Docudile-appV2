package com.docudile.app.services;

import com.docudile.app.services.impl.DocumentStructureClassificationServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * Created by FrancAnthony on 3/1/2016.
 */
public interface DocumentStructureClassificationService {

    public Map<Integer, String> labelParts(List<String> lines);

    public String classify(List<String> labeledParts);

}
