package com.jacob.capstone.dao;

import com.jacob.capstone.documents.Chunker;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChunkRepository extends CassandraRepository<Chunker, String> {
}
