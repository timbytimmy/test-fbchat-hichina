package com.hichina.admin.hichinaadminbackend.repository;

import com.hichina.admin.hichinaadminbackend.model.mongo.HomePostImage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HomePostImageRepository extends MongoRepository<HomePostImage, String> {
}
