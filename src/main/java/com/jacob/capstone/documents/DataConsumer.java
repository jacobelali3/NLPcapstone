package com.jacob.capstone.documents;

import lombok.Getter;
import lombok.Setter;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * @author JacobElali
 * This object defines training data parameters
 */
@Getter
@Setter
public class DataConsumer {

    private String id;
    @SerializedName("captions")
    private String captions;
    @SerializedName("hashtags")
    private List<String> hashtags = null;
    @SerializedName("alt")
    private String alt;



}
