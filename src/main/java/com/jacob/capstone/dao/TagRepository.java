package com.jacob.capstone.dao;

import com.jacob.capstone.documents.Tagger;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends CassandraRepository<Tagger, String> {
}
