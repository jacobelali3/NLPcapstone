package com.jacob.capstone.rest;

import com.jacob.capstone.services.CaptionService;
import com.jacob.capstone.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/consumer")
public class DataController {

    @Autowired
    private DataService dataService;

        @RequestMapping(path = "/createCaptionIndex", method = RequestMethod.GET)
    public ResponseEntity<?> createCaptionIndex() {
        dataService.createCaptionIndex();
        return ResponseEntity.status(HttpStatus.OK).body("Caption Index Created");
    }
    @RequestMapping(path = "/createHashtagIndex", method = RequestMethod.GET)
    public ResponseEntity<?> createHashtagIndex() {
        dataService.createHashtagIndex();
        return ResponseEntity.status(HttpStatus.OK).body("Hashtag Index Created");
    }
    @RequestMapping(path = "/createAltIndex", method = RequestMethod.GET)
    public ResponseEntity<?> createAltIndex() {
        dataService.createAltIndex();
        return ResponseEntity.status(HttpStatus.OK).body("Alt Index created");
    }

    @PostMapping(value = "/postData")
    public ResponseEntity<String> postTrainerData(@RequestParam (name = "file") MultipartFile file, @RequestParam (name = "name") String name) throws IOException {

        return dataService.saveTrainingData(file, name);

    }


}
