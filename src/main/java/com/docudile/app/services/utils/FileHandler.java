/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.docudile.app.services.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author franc
 */
public class FileHandler {
    
    public static Map<String, List<String>> readAllFiles(String folder) throws IOException {
        ArrayList<String> filenames = getFilePaths(folder);
        Map<String, List<String>> files = new HashMap<>();
        
        for (String filename : filenames) {
            FileReader fr = new FileReader(folder + "/" + filename);
            BufferedReader br = new BufferedReader(fr);
            ArrayList<String> lines = new ArrayList<String>();
            String temp;

            while ((temp = br.readLine()) != null) {
                if (StringUtils.isNotEmpty(temp)) {
                    lines.add(temp);
                }
            }
            files.put(filename.split("\\.")[0], lines);
            br.close();
            fr.close();
        }
        return files;
    }
    
    private static ArrayList<String> getFilePaths(String folderPath) {
        ArrayList<String> filenames = new ArrayList<>();
        File folder = new File(folderPath);

        for (File filename : folder.listFiles()) {
            if (filename.isFile()) {
                filenames.add(filename.getName());
            }
        }
        return filenames;
    }

}
