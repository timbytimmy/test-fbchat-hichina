package com.hichina.main.back.hichinamainback.repository;

import com.hichina.main.back.hichinamainback.model.mongo.HomePostImage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HomePostImageRepository extends MongoRepository<HomePostImage, String> {
}
