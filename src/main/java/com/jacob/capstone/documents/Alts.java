package com.jacob.capstone.documents;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


/**
 * @author JacobElali
 * This Pojo defines the stages of processing Alts
 */
@Getter
@Setter
@Document(indexName = "alts", createIndex = true)
public class Alts {
    @Id
    private String dataSetName;

    private String[] alts;
    private String[] sentencedAlts;
    private String[] tokenizedAlts;
    private String[] stemmedAlts;
    private String[] taggedAlts;
}
