package com.hichina.main.back.hichinamainback.repository;

import com.hichina.main.back.hichinamainback.model.mongo.BaiduTranslateCache;
import com.hichina.main.back.hichinamainback.model.mongo.BlogSlideImage;
import com.hichina.main.back.hichinamainback.model.mongo.PageObjectView;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface BaiduTranslateCacheRepository extends MongoRepository<BaiduTranslateCache, String> {
    List<BaiduTranslateCache> findAll();

    long count();

    @Query(value = "{ 'translationKey' : {$eq:?0} }")
    List<BaiduTranslateCache> queryByTranslationKey(String translationKey);

}
