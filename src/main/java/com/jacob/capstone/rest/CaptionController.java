package com.jacob.capstone.rest;

import com.jacob.capstone.services.CaptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/caption")
public class CaptionController {

    @Autowired
    private CaptionService captionService;

    @PostMapping(value = "/detectCaptionSentences")
    public ResponseEntity<String> detectCaptionSentences(@RequestParam(name = "name") String name) throws IOException {
        return captionService.detectCaptionSentences(name);

    }
    @PostMapping(value = "/tokenizeCaptionSentences")
    public ResponseEntity<String> tokenizeCaptionSentences(@RequestParam (name = "name") String name) throws IOException {

        return captionService.tokenizeCaptionSentences(name);

    }
    @PostMapping(value = "/stemCaptionTokens")
    public ResponseEntity<String> stemCaptionTokens(@RequestParam (name = "name") String name) throws IOException {

        return captionService.stemCaptionTokens(name);

    }
}
