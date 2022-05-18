package com.jacob.capstone.dao;

import com.jacob.capstone.documents.Alts;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AltRepository extends ElasticsearchRepository<Alts, String> {

}
