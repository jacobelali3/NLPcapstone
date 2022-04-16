package com.jacob.capstone.dao;

import com.jacob.capstone.documents.Stemmer;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StemmRepository extends CassandraRepository<Stemmer, String> {
}
