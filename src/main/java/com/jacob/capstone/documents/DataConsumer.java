package com.jacob.capstone.documents;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.List;

/**
 * @author JacobElali3
 * This object defines training data parameters
 */
@Table
@Getter
@Setter
public class DataConsumer implements Serializable {

    @PrimaryKey
    private String id;
    private String name;
    private Boolean topPostsOnly;
    private String profilePicUrl;
    private int postsCount;
    private List<String> topPosts;

}
