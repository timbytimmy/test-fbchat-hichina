package com.hichina.admin.hichinaadminbackend.mapper;

import com.hichina.admin.hichinaadminbackend.model.DTO.DestinationShortDTO;
import com.hichina.admin.hichinaadminbackend.model.Destination;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "destinationMapper")
public interface DestinationMapper {
    @Insert("INSERT INTO destination(destination_id, destination_name, level, parent_id, description, destination_profile_image, created_date) VALUES(#{destinationId},#{destinationName}, #{level}, #{parentId}, #{description}, #{destinationProfileImage}, #{createdDate})")
    void insert(Destination destination);

    @Delete("<script>" +
            "Delete FROM destination WHERE destination_id in \n" +
            "    <foreach item='item' collection='destinationIds' open='(' separator=',' close=')'>\n" +
            "    #{item}" +
            "    </foreach>" +
            "</script>")
    void batchDelete(List<String> destinationIds);

    @Update("update destination SET destination_name=#{destinationName}, level=#{level}, parent_id=#{parentId}, description=#{description}, destination_profile_image=#{destinationProfileImage}, created_date=#{createdDate} where destination_id=#{destinationId}")
    void update(Destination destination);

    @Select("select * from destination order by created_date desc")
    List<Destination> findAllDestinations();

    @Select("select * from destination where level=#{level} order by created_date desc")
    List<Destination> findDestinationsByLevel(Integer level);

    @Select("select * from destination where destination_id=#{id}")
    List<Destination> findDestinationById(String id);

    @Select("select count(*) from destination")
    Integer count();

    @Select("select count(*) from destination where level=#{level}")
    Integer countByLevel(Integer level);

    @Select("select * from destination where destination_name like CONCAT('%',CONCAT(#{query},'%')) or description like CONCAT('%',CONCAT(#{query},'%')) order by created_date desc")
    List<Destination> findDestinationsByQuery(String query);

    @Select("select destination_id, destination_name from destination where destination_name like CONCAT('%',CONCAT(#{query},'%')) order by created_date desc")
    List<DestinationShortDTO> findDestinationShortByQuery(String query);

    @Select("select * from destination where destination_name like CONCAT('%',CONCAT(#{query},'%')) or description like CONCAT('%',CONCAT(#{query},'%')) and level=#{level} order by created_date desc")
    List<Destination> findDestinationsByQueryAndLevel(String query, Integer level);

    @Select("select count(*) from destination where destination_name like CONCAT('%',CONCAT(#{query},'%')) or description like CONCAT('%',CONCAT(#{query},'%'))")
    Integer countByQuery(String query);

    @Select("select count(*) from destination where destination_name like CONCAT('%',CONCAT(#{query},'%')) or description like CONCAT('%',CONCAT(#{query},'%')) and leve=#{level}")
    Integer countByQueryAndLevel(String query, Integer level);
}
