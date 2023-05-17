package com.hichina.admin.hichinaadminbackend.mapper;

import com.hichina.admin.hichinaadminbackend.model.ProductAttribute;
import com.hichina.admin.hichinaadminbackend.model.ProductType;
import com.hichina.admin.hichinaadminbackend.model.ProductTypeAttributeMapping;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "productTypeMapper")
public interface ProductTypeMapper {
    @Select("select * from hichina_product_type where product_type_name like CONCAT('%',CONCAT(#{query},'%'))")
    List<ProductType> findProductTypeByQuery(String query);

    @Insert("INSERT INTO product_type_attribute_mapping(attribute_id, product_type_id, sequence) VALUES(#{attributeId},#{productTypeId}, #{sequence})")
    void bindAttr(ProductTypeAttributeMapping productTypeAttributeMapping);

    @Delete("delete from product_type_attribute_mapping where product_type_id=#{productTypeId} and attribute_id=#{attributeId}")
    void unbindAttr(String productTypeId, String attributeId);

    @Select("select count(*) from hichina_product_type where product_type_name like CONCAT('%',CONCAT(#{query},'%'))")
    Integer countByQuery(String query);

    @Select("select * from hichina_product_type")
    List<ProductType> findAllProductType();

    @Select("select att.attribute_id , att.attribute_name , att.data_type  from product_attribute att inner join hichina_product_type tp\n" +
            "inner join product_type_attribute_mapping mp on mp.product_type_id = tp.product_type_id and mp.attribute_id = att.attribute_id \n" +
            "where tp.product_type_id  = #{productTypeId} order by mp.sequence")
    List<ProductAttribute> findAttributesByProductType(String productTypeId);

    @Select("select count(*) from hichina_product_type")
    Integer count();

    @Insert("INSERT INTO hichina_product_type(product_type_id, product_type_name, product_type_description) VALUES(#{productTypeId},#{productTypeName}, #{productTypeDescription})")
    @Options(keyProperty="product_type_id")
    void insert(ProductType productType);

    @Select("<script>" +
            "Delete FROM hichina_product_type WHERE product_type_id in \n" +
            "    <foreach item='item' collection='productTypeIds' open='(' separator=',' close=')'>\n" +
            "    #{item}" +
            "    </foreach>" +
            "</script>")
    void batchDelete(List<String> productTypeIds);

}
