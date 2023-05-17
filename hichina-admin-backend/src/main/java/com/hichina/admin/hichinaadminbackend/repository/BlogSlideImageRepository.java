package com.hichina.admin.hichinaadminbackend.repository;

import com.hichina.admin.hichinaadminbackend.model.mongo.BlogSlideImage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BlogSlideImageRepository extends MongoRepository<BlogSlideImage, String> {
    List<BlogSlideImage> findAll();

    public long count();
}
