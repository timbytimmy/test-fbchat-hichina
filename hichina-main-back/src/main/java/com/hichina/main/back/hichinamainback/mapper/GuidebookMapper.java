package com.hichina.main.back.hichinamainback.mapper;

import com.hichina.main.back.hichinamainback.model.DTO.GuidebookListDTO;
import com.hichina.main.back.hichinamainback.model.Guidebook;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "guidebookMapper")
public interface GuidebookMapper {

    @Select("select g.guide_id , g.download_url , g.cover_image_url , g.short_description , g.destination_id , d.destination_name  from guidebook g inner join destination d on d.destination_id = g.destination_id where d.destination_name like CONCAT('%',CONCAT(#{query},'%')) or g.short_description like CONCAT('%',CONCAT(#{query},'%')) order by g.created_date desc")
    List<GuidebookListDTO> findGuidebookByQuery(String query);


    @Select("select g.guide_id , g.download_url , g.cover_image_url , g.short_description , g.destination_id , d.destination_name  from guidebook g inner join destination d on d.destination_id = g.destination_id where g.guide_id=#{guideId}")
    List<GuidebookListDTO> findGuidebookByGuideId(String guideId);

    @Select("select g.guide_id , g.download_url , g.cover_image_url , g.short_description , g.destination_id , d.destination_name  from guidebook g inner join destination d on d.destination_id = g.destination_id order by g.created_date desc")
    List<GuidebookListDTO> findAllGuidebook();

    @Select("select count(*) from guidebook")
    Integer count();

    @Select("select count(*) from guidebook g inner join destination d on d.destination_id = g.destination_id where d.destination_name like CONCAT('%',CONCAT(#{query},'%')) or g.short_description like CONCAT('%',CONCAT(#{query},'%'))")
    Integer countByQuery(String query);
}
