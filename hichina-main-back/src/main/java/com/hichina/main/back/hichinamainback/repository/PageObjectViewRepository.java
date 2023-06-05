package com.hichina.main.back.hichinamainback.repository;

import com.hichina.main.back.hichinamainback.model.mongo.PageObjectView;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PageObjectViewRepository extends MongoRepository<PageObjectView, String> {
    @Query(value = "{ $and: [ { 'objId' : {$eq:?0} }, { 'objType' : {$eq:?1} } ] }")
    List<PageObjectView> queryByIdAndType(String idQuery, String objTypeQuery);


}
