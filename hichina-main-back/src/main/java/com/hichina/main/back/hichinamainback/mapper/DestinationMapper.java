package com.hichina.main.back.hichinamainback.mapper;

import com.hichina.main.back.hichinamainback.model.DTO.DestinationProfileDTO;
import com.hichina.main.back.hichinamainback.model.DTO.DestinationWithGuidebook;
import com.hichina.main.back.hichinamainback.model.Destination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "destinationMapper")
public interface DestinationMapper {
    @Select("select c.destination_id, c.destination_name, c.destination_profile_image, p.destination_name  as parent_destination_name from destination c inner join destination p on p.destination_id = c.parent_id  where c.level in (6, 7) order by rand() limit #{count}")
    List<DestinationProfileDTO> findRandomDestinationsByCount(int count);

    @Select("select c.destination_id, c.destination_name, c.destination_profile_image, p.destination_name  as parent_destination_name from destination c left join destination p on p.destination_id = c.parent_id  where c.level in (5, 6, 7) order by c.created_date desc")
    List<DestinationProfileDTO> findAllDestinations();

    @Select("select c.destination_id, c.destination_name, c.destination_profile_image, p.destination_name  as parent_destination_name from destination c left join destination p on p.destination_id = c.parent_id  where c.level in (5, 6, 7) and c.destination_name like CONCAT('%',CONCAT(#{query},'%')) order by c.created_date desc")
    List<DestinationProfileDTO> findAllDestinationsByQuery(String query);

    @Select("select count(*) from (select c.destination_id, c.destination_name, c.destination_profile_image, p.destination_name  as parent_destination_name from destination c left join destination p on p.destination_id = c.parent_id  where c.level in (5, 6, 7) and c.destination_name like CONCAT('%',CONCAT(#{query},'%')) ) cc")
    Integer countAllDestinationsByQuery(String query);

    @Select("select count(*) from (select c.destination_id, c.destination_name, c.destination_profile_image, p.destination_name  as parent_destination_name from destination c left join destination p on p.destination_id = c.parent_id  where c.level in (5, 6, 7) ) cc")
    Integer countAllDestinations();

    @Select("select * from destination where parent_id=#{destinationId}")
    List<Destination> findDirectChildById(String destinationId);

    @Select("select d.destination_profile_image ,d.destination_id , d.destination_name, d.description, g.download_url  from destination d  left join guidebook g on g.destination_id = d.destination_id  where d.destination_id =#{destinationId}")
    List<DestinationWithGuidebook> findDestinationWithGuidebookById(String destinationId);
}
