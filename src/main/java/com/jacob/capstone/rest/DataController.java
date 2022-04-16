package com.jacob.capstone.rest;

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

    @PostMapping(value = "/postData")
    public ResponseEntity<HashMap<String, Object>> postTrainerData(@RequestParam (name = "file") MultipartFile file) throws IOException {
        HashMap<String, Object> response = new HashMap<String, Object>();
        dataService.saveTrainingData(file);
        response.put("message", "Training data posted successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
