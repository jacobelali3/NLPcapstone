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
    public ResponseEntity<HashMap<String, Object>> postTrainerData(@RequestParam (name = "file") MultipartFile file, @RequestParam (name = "name") String name) throws IOException {
        HashMap<String, Object> response = new HashMap<>();
        String result = dataService.saveTrainingData(file, name);
        response.put("message", result);

        if(result == null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        if(result.contains("Could not")) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        if(result.contains("Bad Request.")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

}
