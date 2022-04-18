package com.jacob.capstone.dao;

import com.jacob.capstone.documents.Captions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CaptionRepository extends MongoRepository<Captions, UUID> {
}
