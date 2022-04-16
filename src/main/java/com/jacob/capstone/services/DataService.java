package com.jacob.capstone.services;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.jacob.capstone.dao.DataRepository;
import com.jacob.capstone.documents.DataConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author JacobElali3
 * This service transforms input training data into repositories.
 */
@Service
@Slf4j
public class DataService {

    @Autowired
    private DataRepository dataRepository;



    @Async
    public Future<String> saveTrainingData(MultipartFile multipartFile) throws IOException
    {
        List<String> captionList = new ArrayList<>();
        List<String> hashtagList = new ArrayList<>();
        InputStreamReader isr = new InputStreamReader(multipartFile.getInputStream(), "UTF-8");
        Gson gson = new Gson();
        try {
            DataConsumer dataConsumer = gson.fromJson(isr, DataConsumer.class);


        }
        catch(Exception e)
        {
            log.info("Could not deserialize GSON");
        }

        return new AsyncResult<String>("Upload process complete.");

    }

}
