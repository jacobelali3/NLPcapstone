package com.jacob.capstone.documents;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author JacobElali
 * This Pojo defines the stages of processing hashtags
 */
@Getter
@Setter
@Document(indexName = "hashtags", createIndex = true)
public class Hashtags {
    @Id
    private String dataSetName;

    private String[] hashtags;
    private String[] sentencedHashtags;
    private String[] tokenizedHashtags;
    private String[] stemmedHashtags;
    private String[] taggedHashtags;
}
