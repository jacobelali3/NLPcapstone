package com.jacob.capstone.dao;

import com.jacob.capstone.documents.Hashtags;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HashtagRepository extends ElasticsearchRepository<Hashtags, String> {

}
