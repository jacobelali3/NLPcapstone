package com.jacob.capstone.dao;

import com.jacob.capstone.documents.Lemmatizer;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LemmRepository extends CassandraRepository<Lemmatizer, String> {
}
