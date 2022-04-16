package com.jacob.capstone.dao;

import com.jacob.capstone.documents.Tokenizer;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CassandraRepository <Tokenizer, String> {
}
