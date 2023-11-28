package com.paper.repositories;

import com.paper.domain.MongoImage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends MongoRepository<MongoImage, String> {



}
