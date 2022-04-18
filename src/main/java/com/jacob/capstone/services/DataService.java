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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author JacobElali3
 * This service transforms input training data into repositories.
 */
@Transactional
@Service
@Slf4j
public class DataService {

    @Autowired
    private AltRepository altRepository;
    @Autowired
    private CaptionRepository captionRepository;
    @Autowired
    private HashtagRepository hashtagRepository;



    @Async
    public String saveTrainingData(MultipartFile multipartFile, String name) throws IOException
    {
        if(name == null) return "Bad Request. Dataset name MUST be provided with your training data.";

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
            String[] captionArrayList = captionList.toArray(new String[captionList.size()]);
            String[] hashtagArrayList = hashtagList.toArray(new String[hashtagList.size()]);
            String[] altArrayList = altList.toArray(new String[altList.size()]);

            if(altArrayList.length < 10000)  return "Bad Request. Not enough alts exist in training data\nAlt Entries:" +altArrayList.length;
            if(captionArrayList.length < 10000 )  return "Bad Request. Not enough captions exist in training data\nHashtag Entries:" +hashtagArrayList.length;
            if(hashtagArrayList.length < 10000 )  return "Bad Request. Not enough hashtags exist in training data\nCaption Entries:" +captionArrayList.length;

            Alts alts = new Alts();
            alts.setDataSetName(name);
            alts.setAlts(altArrayList);

            Captions captions = new Captions();
            captions.setDataSetName(name);
            captions.setCaptions(captionArrayList);

            Hashtags hashtags = new Hashtags();
            hashtags.setDataSetName(name);
            hashtags.setHashtags(hashtagArrayList);

            altRepository.save(alts);
            hashtagRepository.save(hashtags);
            captionRepository.save(captions);

        }
        catch(JsonParseException e)
        {
            log.info("Could not deserialize JSON, please make sure you enter an array of objects.");
            return "Could not deserialize JSON, please make sure you enter an array of objects.";
        }
        catch(DataAccessException e)
        {
            log.info("Problem accessing repository.");
        }
        catch(Exception e)
        {
            log.info("Could not process training data.");
        }

        return "Upload process complete.";

    }


}
