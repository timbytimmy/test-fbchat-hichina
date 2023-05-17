package com.hichina.admin.hichinaadminbackend.repository;

import com.hichina.admin.hichinaadminbackend.model.mongo.HomeSlideImage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface HomeSlideImageRepository extends MongoRepository<HomeSlideImage, String> {

    List<HomeSlideImage> findAll();

    public long count();

}
