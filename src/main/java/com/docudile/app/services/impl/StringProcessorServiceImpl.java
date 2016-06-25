package com.docudile.app.services.impl;

import com.docudile.app.services.StringProcessorService;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.Word;
import net.sf.extjwnl.dictionary.Dictionary;
import net.sf.extjwnl.dictionary.MorphologicalProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weka.core.Stopwords;

import java.util.*;

/**
 * Created by franc on 5/3/2016.
 */
@Service("stringProcessorService")
@Transactional
public class StringProcessorServiceImpl implements StringProcessorService {

    private final String DELIMETER = " ";

    private final HashMap<String, String> baseForms = new HashMap<>();

    private final HashMap<String, Set<String>> synonyms = new HashMap<>();

    @Override
    public List<String> process(String line) {
        List<String> processed = new ArrayList<>();
        for (String tempToken : tokenize(line)) {
            processed.add(stem((tempToken)));
        }
        return processed;
    }

    @Override
    public List<String> getSynonyms(String word) {
        return new ArrayList<>(synonyms(word));
    }

    private List<String> tokenize(String line) {
        StringTokenizer stkn = new StringTokenizer(line.toLowerCase(), DELIMETER);
        List<String> tokens = new ArrayList<>();
        while (stkn.hasMoreTokens()) {
            String tempToken = stkn.nextToken();
            if (checkIfInDictionary(tempToken) && !Stopwords.isStopword(tempToken) && !StringUtils.isNumeric(tempToken)) {
                tokens.add(tempToken);
            }
        }
        return tokens;
    }

    private boolean checkIfInDictionary(String word) {
        try {
            Dictionary dictionary = Dictionary.getDefaultResourceInstance();
            for (POS pos : POS.getAllPOS()) {
                IndexWord index = dictionary.getIndexWord(pos, word);
                if (index != null) {
                    return !index.getSenses().isEmpty();
                }
            }
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String stemWordWithWordNet(String word) {
        try {
            Dictionary dictionary = Dictionary.getDefaultResourceInstance();
            MorphologicalProcessor morph = dictionary.getMorphologicalProcessor();
            IndexWord indexWord;
            if (StringUtils.isEmpty(word)) {
                return null;
            }
            indexWord = morph.lookupBaseForm(POS.VERB, word);
            if (indexWord != null && !indexWord.getLemma().equalsIgnoreCase(word)) {
                return indexWord.getLemma();
            }
            indexWord = morph.lookupBaseForm(POS.NOUN, word);
            if (indexWord != null && !indexWord.getLemma().equalsIgnoreCase(word)) {
                return indexWord.getLemma();
            }
            indexWord = morph.lookupBaseForm(POS.ADJECTIVE, word);
            if (indexWord != null && !indexWord.getLemma().equalsIgnoreCase(word)) {
                return indexWord.getLemma();
            }
            indexWord = morph.lookupBaseForm(POS.ADVERB, word);
            if (indexWord != null && !indexWord.getLemma().equalsIgnoreCase(word)) {
                return indexWord.getLemma();
            }
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String stem(String word) {
        if (baseForms.containsKey(word)) {
            return baseForms.get(word);
        } else {
            String stemmedWord = stemWordWithWordNet(word);

            if (stemmedWord != null) {
                baseForms.put(word, stemmedWord);
                return stemmedWord;
            }
            baseForms.put(word, word);
            return word;
        }
    }

    private List<String> getLemmaFromSynset(IndexWord w) throws JWNLException {
        List<String> synWords = new ArrayList<>();
        List<Synset> synsets = w.getSenses();

        for (Synset synset : synsets) {
            for (Word word : synset.getWords()) {
                String temp = word.getLemma();
                if (StringUtils.isNotBlank(temp)) {
                    synWords.add(temp);
                }
            }
        }
        return synWords;
    }

    private Set<String> retrieveSynsetFromWordNet(String word) {
        Set<String> synWords = new HashSet<>();
        try {
            Dictionary dictionary = Dictionary.getDefaultResourceInstance();
            IndexWord indexWord;

            indexWord = dictionary.getIndexWord(POS.NOUN, word);
            if (indexWord != null) {
                synWords.addAll(getLemmaFromSynset(indexWord));
            }
            indexWord = dictionary.getIndexWord(POS.VERB, word);
            if (indexWord != null) {
                synWords.addAll(getLemmaFromSynset(indexWord));
            }
            indexWord = dictionary.getIndexWord(POS.ADVERB, word);
            if (indexWord != null) {
                synWords.addAll(getLemmaFromSynset(indexWord));
            }
            indexWord = dictionary.getIndexWord(POS.ADJECTIVE, word);
            if (indexWord != null) {
                synWords.addAll(getLemmaFromSynset(indexWord));
            }
            synWords.remove(word);
            return synWords;
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        return synWords;
    }

    public Set<String> synonyms(String word) {
        if (synonyms.containsKey(word)) {
            return synonyms.get(word);
        } else {
            Set<String> synWords = retrieveSynsetFromWordNet(word);
            synonyms.put(word, synWords);
            return synWords;
        }
    }
}
