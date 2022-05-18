package com.jacob.capstone.services;

import com.jacob.capstone.dao.CaptionRepository;
import com.jacob.capstone.documents.Alts;
import com.jacob.capstone.documents.Captions;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional
@Service
@Slf4j
public class CaptionService {

    @Autowired
    private CaptionRepository captionRepository;

    private final String sentenceModelPath = "./src/NLP Models/opennlp-en-ud-ewt-sentence-1.0-1.9.3.bin";
    private final String tokenModelpath = "./src/NLP Models/opennlp-en-ud-ewt-tokens-1.0-1.9.3.bin";
    private final String posModelPath = "./src/NLP Models/opennlp-en-ud-ewt-pos-1.0-1.9.3.bin";
    private final String lemmModelPath = "./src/NLP Models/en-lemmatizer.bin";

    public ResponseEntity<String> detectCaptionSentences(String name)
    {
        try{
            log.info("Detecting sentences");
            Optional<Captions> captions = captionRepository.findById(name);
            if(captions.isEmpty())  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid dataset name provided. Be sure to add -caption to your original name Eg: dataSet1-caption.");
            if(captions.get().getCaptions() == null || captions.get().getCaptions().length <= 0)  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Provided dataset does not have any captions populated. Please populate using /postData first.");
            File file = new File(sentenceModelPath);
            InputStream modelIn = new FileInputStream(file);
            SentenceModel model  = new SentenceModel(modelIn);
            SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
            String sentences = Arrays.toString(captions.get().getCaptions());
            captions.get().setSentencedCaptions(sentenceDetector.sentDetect(sentences));
            captionRepository.save(captions.get());
            return ResponseEntity.status(HttpStatus.OK).body("Sentences successfully split. Saved under " + captions.get().getDataSetName() + "'s sentencedCaptions");

        }catch(FileNotFoundException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not find sentence model within directory.");
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    public ResponseEntity<String> tokenizeCaptionSentences(String name){
        try{
            Optional<Captions> captions = captionRepository.findById(name);
            if(captions.isEmpty())  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid dataset name provided. Be sure to add -caption to your original name Eg: dataSet1-caption.");
            if(captions.get().getSentencedCaptions() == null || captions.get().getSentencedCaptions().length <= 0)  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Provided dataset does not have any sentenced captions populated. Please populate using /detectCaptionSentences first.");

            String[] captionSentences = captions.get().getSentencedCaptions();
            File file = new File(tokenModelpath);
            InputStream modelIn = new FileInputStream(file);
            TokenizerModel model = new TokenizerModel(modelIn);
            Tokenizer tokenizer = new TokenizerME(model);
            String tokens = Arrays.toString(captionSentences);
            String[] tokenized = tokenizer.tokenize(tokens);
            log.info(Arrays.toString(tokenized));
            return ResponseEntity.status(HttpStatus.OK).body("Sentences successfully tokenized. Saved under " + captions.get().getDataSetName() + "'s tokenizedCaptions");
        }catch(FileNotFoundException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not find token model within directory.");
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public ResponseEntity<String> stemCaptionTokens(String name){

        try{
            Optional<Captions> captions = captionRepository.findById(name);
            if(captions.isEmpty())  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid dataset name provided. Be sure to add -caption to your original name Eg: dataSet1-caption.");
            if(captions.get().getTokenizedCaptions() == null || captions.get().getTokenizedCaptions().length <= 0)  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Provided dataset does not have any tokenized captions populated. Please populate using /tokenizeCaptionSentences first.");

            String[] tokenList = captions.get().getTokenizedCaptions();
            List<String> stemmedTokenList = new ArrayList<>();
            PorterStemmer stem = new PorterStemmer();
            for(String token : tokenList){
                if(Objects.equals(token, "[") || Objects.equals(token, "]")) continue;
                stemmedTokenList.add(stem.stem(token));
            }

            captions.get().setStemmedCaptions(stemmedTokenList.toArray(String[]::new));
            captionRepository.save(captions.get());
            return ResponseEntity.status(HttpStatus.OK).body("Sentences successfully Stemmed. Saved under " + captions.get().getDataSetName() + "'s stemmedCaptions");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }


    }
//
//    public String[] speechTagCaptionToken(String name){
//
//        try{
//            log.info("Speech tagging Caption Tokens");
//            String[] stemmedTokens = stemCaptionsTokens(captions);
//            File file = new File(posModelPath);
//            InputStream modelIn = new FileInputStream(file);
//            POSModel model = new POSModel(modelIn);
//            POSTaggerME tagger = new POSTaggerME(model);
//            return tagger.tag(stemmedTokens);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return new String[0];
//    }
//    public String lemmatizeTokens(String name){
//
//        try{
//            Captions captions = captionRepository.findByDataSetName(name);
//            String[] stemmedTokens = stemCaptionsTokens(captions);
//            String[] speechTaggedTokens = speechTagCaptionToken(captions);
//            File file = new File(lemmModelPath);
//            InputStream modelIn = new FileInputStream(file);
//            LemmatizerModel model = new LemmatizerModel(modelIn);
//            LemmatizerME lemmatizer = new LemmatizerME(model);
//            String[] lemmas = lemmatizer.lemmatize(stemmedTokens, speechTaggedTokens);
//            log.info(lemmas[0]);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

}
