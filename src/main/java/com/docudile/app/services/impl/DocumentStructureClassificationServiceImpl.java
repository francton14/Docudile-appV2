package com.docudile.app.services.impl;

import com.docudile.app.services.DocumentStructureClassificationService;
import com.docudile.app.services.utils.FileHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by FrancAnthony on 3/2/2016.
 */
@Service("documentStructureClassificationService")
@Transactional
@PropertySource({"classpath:/storage.properties"})
public class DocumentStructureClassificationServiceImpl implements DocumentStructureClassificationService {

    @Autowired
    private Environment environment;

    @Override
    public Map<Integer, String> tag(List<String> lines) {
        Map<Integer, String> tagged = new HashMap<>();
        Map<String, List<String>> tags = getTags(environment.getProperty("storage.base_tags"));
        int i = 0;
        for (String line : lines) {
            System.out.println(line);
            boolean found = false;
            for (String tag : tags.keySet()) {
                for (String rule : tags.get(tag)) {
                    Pattern pattern = Pattern.compile(rule);
                    if (pattern.matcher(line).matches()) {
                        found = true;
                        tagged.put(i, tag);
                        break;
                    }
                }
                if (found) {
                    break;
                }
            }
            if (!found) {
                if (StringUtils.containsIgnoreCase(line, "Office") || StringUtils.containsIgnoreCase(line, "College") || StringUtils.containsIgnoreCase(line, "Organization") || StringUtils.containsIgnoreCase(line, "Institute")) {
                    tagged.put(i, "OFFICE");
                }
            }
            if (found) {
                if (tagged.get(i).equals("SALUTATION") || tagged.get(i).equals("SUBJECT")) {
                    break;
                }
            }
            i++;
        }
        return tagged;
    }

    @Override
    public String classify(List<String> tags) {
        try {
            Map<String, List<String>> modelData = FileHandler.readAllFiles(environment.getProperty("storage.classifier"));
            Map<String, Integer> differenceMap = new HashMap<>();
            for (String key : modelData.keySet()) {
                List<String> tempModelTags = new ArrayList<>(modelData.get(key));
                List<String> tempTags = new ArrayList<>(tags);
                tempModelTags.removeAll(tempTags);
                int difference = modelData.get(key).size() - tempModelTags.size();
                System.out.println("Difference with " + key + ": " + difference);
                if (difference > 0) {
                    differenceMap.put(key, difference);
                }
            }
            if (!differenceMap.isEmpty()) {
                return getLeastDifference(differenceMap).toUpperCase();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(String path) {
        return new File(path).delete();
    }

    private Map<String, List<String>> getTags(String location) {
        Map<String, List<String>> tags = new HashMap<>();
        try {
            FileReader fileReader = new FileReader(location);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                String[] tokens = temp.split(":=");
                if (tokens.length >= 2) {
                    if (tags.containsKey(tokens[0])) {
                        List<String> tempTags = tags.get(tokens[0]);
                        tempTags.add(tokens[1]);
                    } else {
                        List<String> tempTags = new ArrayList<>();
                        tempTags.add(tokens[1]);
                        tags.put(tokens[0], tempTags);
                    }
                }
            }
            return tags;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getLeastDifference(Map<String, Integer> differenceMap) {
        List<String> keys = new ArrayList<>(differenceMap.keySet());
        String maxKey = keys.get(0);
        int maxDifference = differenceMap.get(maxKey);

        for (int i = 1; i < keys.size(); i++) {
            if (maxDifference < differenceMap.get(keys.get(i))) {
                maxKey = keys.get(i);
                maxDifference = differenceMap.get(maxKey);
            }
        }
        return maxKey;
    }

}
