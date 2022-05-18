package com.jacob.capstone.services;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.jacob.capstone.dao.AltRepository;
import com.jacob.capstone.dao.CaptionRepository;
import com.jacob.capstone.dao.HashtagRepository;
import com.jacob.capstone.documents.Alts;
import com.jacob.capstone.documents.Captions;
import com.jacob.capstone.documents.DataConsumer;
import com.jacob.capstone.documents.Hashtags;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author JacobElali
 * This service transforms input training data into repositories.
 */
@Transactional
@Service
@Slf4j
public class DataService {

    @Autowired
    private CaptionRepository captionRepository;
    @Autowired
    private HashtagRepository hashtagRepository;
    @Autowired
    private AltRepository altRepository;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    private final String sentenceModelPath = "./src/NLP Models/opennlp-en-ud-ewt-sentence-1.0-1.9.3.bin";
    private final String tokenModelpath = "./src/NLP Models/opennlp-en-ud-ewt-tokens-1.0-1.9.3.bin";




    public void createCaptionIndex() {
        if (!elasticsearchOperations.indexOps(Captions.class).exists()) {
            elasticsearchOperations.indexOps(Captions.class).create();
            log.info("Captions Index Created");
        }
    }
    public void createHashtagIndex() {
        if (!elasticsearchOperations.indexOps(Hashtags.class).exists()) {
            elasticsearchOperations.indexOps(Hashtags.class).create();
            log.info("Hashtag Index Created");
        }
    }
    public void createAltIndex() {
        if (!elasticsearchOperations.indexOps(Alts.class).exists()) {
            elasticsearchOperations.indexOps(Alts.class).create();
            log.info("Alt Index Created");
        }
    }

    public ResponseEntity<String> saveTrainingData(MultipartFile multipartFile, String name) throws IOException
    {
        if(name == null) ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request. Dataset name MUST be provided with your training data.");

        List<String> captionList = new ArrayList<>();
        List<String> hashtagList = new ArrayList<>();
        List<String> altList =  new ArrayList<>();
        InputStreamReader isr = new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8);
        Gson gson = new Gson();
        try {

            Type listType = new TypeToken<ArrayList<DataConsumer>>(){}.getType();
            List<DataConsumer> dataConsumerList = gson.fromJson(isr, listType);
            for(DataConsumer post : dataConsumerList)
            {
                captionList.add(post.getCaptions());
                hashtagList.addAll(post.getHashtags());
                altList.add(post.getAlt());
            }

            captionList.removeIf(caption -> caption == null || caption.isEmpty());
            hashtagList.removeIf(hashtag -> hashtag == null || hashtag.isEmpty());
            altList.removeIf(alt -> alt == null || alt.isEmpty());

            String[] captionArrayList = captionList.toArray(new String[captionList.size()]);
            String[] hashtagArrayList = hashtagList.toArray(new String[hashtagList.size()]);
            String[] altArrayList = altList.toArray(new String[altList.size()]);

            if(altArrayList.length < 10000)  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request. Not enough alts exist in training data\nAlt Entries:" +altArrayList.length);
            if(captionArrayList.length < 10000 )  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request. Not enough captions exist in training data\nHashtag Entries:" +hashtagArrayList.length);
            if(hashtagArrayList.length < 10000 )  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request. Not enough hashtags exist in training data\nCaption Entries:" +captionArrayList.length);

            Alts alts = new Alts();
            alts.setDataSetName(name + "-alt");
            alts.setAlts(altArrayList);

            Captions captions = new Captions();
            captions.setDataSetName(name +"-caption");
            captions.setCaptions(captionArrayList);

            Hashtags hashtags = new Hashtags();
            hashtags.setDataSetName(name + "-hashtag");
            hashtags.setHashtags(hashtagArrayList);

            altRepository.save(alts);
            hashtagRepository.save(hashtags);
            captionRepository.save(captions);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully saved Data set");
        }
        catch(JsonParseException e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not deserialize JSON, please make sure you enter an array of objects.");
        }
        catch(DataAccessException e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error communicating with Database. " +e.getMessage());
        }
        catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not process training data. "+ e.getMessage());
        }



    }



}
