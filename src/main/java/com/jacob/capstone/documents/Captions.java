package com.jacob.capstone.documents;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author JacobElali
 * This Pojo defines the stages of processing captions
 */
@Getter
@Setter
@Document(indexName = "captions", createIndex = true)
public class Captions {
    @Id
    private String dataSetName;

    private String[] captions;
    private String[] sentencedCaptions;
    private String[] tokenizedCaptions;
    private String[] stemmedCaptions;
    private String[] taggedCaptions;
}
