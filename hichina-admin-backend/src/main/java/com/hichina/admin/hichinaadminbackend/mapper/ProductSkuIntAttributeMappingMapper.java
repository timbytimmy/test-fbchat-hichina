package com.hichina.admin.hichinaadminbackend.mapper;

import com.hichina.admin.hichinaadminbackend.model.DTO.ProductPropertyBag;
import com.hichina.admin.hichinaadminbackend.model.ProductSkuIntAttributeMapping;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "productSkuIntAttributeMappingMapper")
public interface ProductSkuIntAttributeMappingMapper {
    @Insert("INSERT INTO product_sku_int_attribute_mapping(sku_id, data_type, attribute_id, attribute_value) VALUES(#{skuId},#{dataType}, #{attributeId}, #{attributeValue})")
    @Options(keyProperty="sku_id")
    void insert(ProductSkuIntAttributeMapping productSkuIntAttributeMapping);

    @Delete("<script>" +
            "Delete FROM product_sku_int_attribute_mapping WHERE sku_id in \n" +
            "    <foreach item='item' collection='toBeDel' open='(' separator=',' close=')'>\n" +
            "    #{item}" +
            "    </foreach>" +
            "</script>")
    void batchDelete(List<String> toBeDel);

    @Select("select ptam.`sequence` , a.attribute_name, pm.attribute_id, pm.attribute_value, pm.data_type from product_sku_int_attribute_mapping pm inner join product_attribute a on a.attribute_id=pm.attribute_id inner join hichina_product hp on hp.sku_id = pm.sku_id inner join product_type_attribute_mapping ptam on ptam.product_type_id = hp.product_type_id and ptam.attribute_id = pm.attribute_id where pm.sku_id=#{skuId}")
    List<ProductPropertyBag> findAttributeValueBySkuId(String skuId);

    @Update("update product_sku_int_attribute_mapping SET attributeValue=#{value} where sku_id=#{skuId} and attribute_id=#{attributeId}")
    void updateBySkuIdAndAttributeId(String skuId, String attributeId, Integer value);

    @Insert("insert into product_sku_int_attribute_mapping(sku_id, data_type, attribute_id, attribute_value) VALUES(#{skuId},#{dataType}, #{attributeId}, #{attributeValue}) ON DUPLICATE KEY UPDATE attribute_value=#{attributeValue}")
    void upsertBySkuIdAndAttributeId(ProductSkuIntAttributeMapping obj);

}
