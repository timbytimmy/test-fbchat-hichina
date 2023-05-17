package com.hichina.main.back.hichinamainback.repository;

import com.hichina.main.back.hichinamainback.model.mongo.HomeSlideImage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HomeSlideImageRepository extends MongoRepository<HomeSlideImage, String> {

    List<HomeSlideImage> findAll();

    public long count();

}
