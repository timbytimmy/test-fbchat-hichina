package com.hichina.admin.hichinaadminbackend.mapper;

import com.hichina.admin.hichinaadminbackend.model.ProductAttribute;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "productAttributeMapper")
public interface ProductAttributeMapper {
    @Insert("INSERT INTO product_attribute(attribute_id, attribute_name, data_type) VALUES(#{attributeId},#{attributeName}, #{dataType})")
    @Options(keyProperty="attribute_id")
    void insert(ProductAttribute productAttribute);

    @Select("select * from product_attribute where attribute_name like CONCAT('%',CONCAT(#{query},'%'))")
    List<ProductAttribute> findAttributesByQuery(String query);

    @Select("select count(*) from product_attribute where attribute_name like CONCAT('%',CONCAT(#{query},'%'))")
    Integer countByQuery(String query);

    @Select("select * from product_attribute")
    List<ProductAttribute> findAllAttributes();

    @Select("select count(*) from product_attribute")
    Integer count();

    @Delete("<script>" +
            "Delete FROM product_attribute WHERE attribute_id in \n" +
            "    <foreach item='item' collection='attributeIds' open='(' separator=',' close=')'>\n" +
            "    #{item}" +
            "    </foreach>" +
            "</script>")
    void batchDelete(List<String> attributeIds);
}
