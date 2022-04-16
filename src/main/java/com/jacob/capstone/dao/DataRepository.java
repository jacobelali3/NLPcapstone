package com.jacob.capstone.dao;

import com.jacob.capstone.documents.DataConsumer;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends CassandraRepository<DataConsumer, String> {


}
