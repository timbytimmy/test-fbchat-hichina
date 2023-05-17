package com.hichina.admin.hichinaadminbackend.mapper;

import com.hichina.admin.hichinaadminbackend.model.DTO.GuidebookListDTO;
import com.hichina.admin.hichinaadminbackend.model.Destination;
import com.hichina.admin.hichinaadminbackend.model.Guidebook;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "guidebookMapper")
public interface GuidebookMapper {
    @Insert("INSERT INTO guidebook(guide_id, download_url, cover_image_url, short_description, destination_id, created_date) VALUES(#{guideId},#{downloadUrl}, #{coverImageUrl}, #{shortDescription}, #{destinationId}, #{createdDate})")
    Long insert(Guidebook guidebook);

    @Delete("<script>" +
            "Delete FROM guidebook WHERE guide_id in \n" +
            "    <foreach item='item' collection='guideIds' open='(' separator=',' close=')'>\n" +
            "    #{item}" +
            "    </foreach>" +
            "</script>")
    void batchDelete(List<String> guideIds);

    @Select("select g.guide_id , g.download_url , g.cover_image_url , g.short_description , g.destination_id , d.destination_name  from guidebook g inner join destination d on d.destination_id = g.destination_id where d.destination_name like CONCAT('%',CONCAT(#{query},'%')) or g.short_description like CONCAT('%',CONCAT(#{query},'%')) order by g.created_date desc")
    List<GuidebookListDTO> findGuidebookByQuery(String query);

    @Select("select * from guidebook where guide_id=#{guideId}")
    List<Guidebook> findRawByGuideId(String guideId);

    @Update("update guidebook SET guide_id=#{guideId}, cover_image_url=#{coverImageUrl}, download_url=#{downloadUrl}, short_description=#{shortDescription}, destination_id=#{destinationId}, created_date=#{createdDate} where guide_id=#{guideId}")
    void update(Guidebook guidebook);

    @Select("select g.guide_id , g.download_url , g.cover_image_url , g.short_description , g.destination_id , d.destination_name  from guidebook g inner join destination d on d.destination_id = g.destination_id where g.guide_id=#{guideId}")
    List<GuidebookListDTO> findGuidebookByGuideId(String guideId);

    @Select("select g.guide_id , g.download_url , g.cover_image_url , g.short_description , g.destination_id , d.destination_name  from guidebook g inner join destination d on d.destination_id = g.destination_id order by g.created_date desc")
    List<GuidebookListDTO> findAllGuidebook();

    @Select("select count(*) from guidebook")
    Integer count();

    @Select("select count(*) from guidebook g inner join destination d on d.destination_id = g.destination_id where d.destination_name like CONCAT('%',CONCAT(#{query},'%')) or g.short_description like CONCAT('%',CONCAT(#{query},'%'))")
    Integer countByQuery(String query);
}
