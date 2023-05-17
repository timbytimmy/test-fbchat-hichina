package com.hichina.main.back.hichinamainback.repository;

import com.hichina.main.back.hichinamainback.model.mongo.BlogSlideImage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BlogSlideImageRepository extends MongoRepository<BlogSlideImage, String> {
    List<BlogSlideImage> findAll();

    long count();
}
