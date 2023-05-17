package com.hichina.main.back.hichinamainback.repository;

import com.hichina.main.back.hichinamainback.model.mongo.OrderMeta;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderMetaRepository extends MongoRepository<OrderMeta, String> {
}
