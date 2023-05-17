package com.hichina.admin.hichinaadminbackend.mapper;

import com.hichina.admin.hichinaadminbackend.model.HichinaLine;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "hichinaLineMapper")
public interface HichinaLineMapper {

    @Insert("INSERT INTO hichina_line(name, public_id, created_date, icon_path, range_in_days, base_price, description) VALUES(#{name},#{publicId}, #{createdDate}, #{iconPath}, #{rangeInDays}, #{basePrice}, #{description})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    Long insert(HichinaLine line);

    @Delete("DELETE FROM hichina_line WHERE id =#{id}")
    void delete(Long id);

    @Update("update hichina_line SET description=#{description}, name=#{name}, icon_path=#{iconPath}, range_in_days=#{rangeInDays}, base_price=#{basePrice} where public_id=#{publicId}")
    void update(HichinaLine line);


    @Select("<script>" +
            "Delete FROM hichina_line WHERE public_id in \n" +
            "    <foreach item='item' collection='publicIds' open='(' separator=',' close=')'>\n" +
            "    #{item}" +
            "    </foreach>" +
            "</script>")
    void batchDelete(List<String> publicIds);

    @Select("select * from hichina_line where name like CONCAT('%',CONCAT(#{query},'%')) or description like CONCAT('%',CONCAT(#{query},'%')) order by created_date desc")
    List<HichinaLine> findLinesByQuery(String query);

    @Select("select * from hichina_line where public_id=#{publicId}")
    List<HichinaLine> findLineByPublicId(String publicId);

    @Select("select count(*) from hichina_line")
    Integer count();

    @Select("select count(*) from hichina_line where name like CONCAT('%',CONCAT(#{query},'%')) or description like CONCAT('%',CONCAT(#{query},'%'))")
    Integer countByQuery(String query);

    @Select("select * from hichina_line order by created_date desc")
    List<HichinaLine> findAllLines();
}
