package com.hichina.main.back.hichinamainback.repository;

import com.hichina.main.back.hichinamainback.model.mongo.HichinaPv;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface PvRepository extends MongoRepository<HichinaPv, String> {


}
