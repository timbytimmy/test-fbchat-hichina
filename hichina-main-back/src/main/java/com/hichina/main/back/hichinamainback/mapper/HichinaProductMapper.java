package com.hichina.main.back.hichinamainback.mapper;

import com.hichina.main.back.hichinamainback.model.DTO.HichinaProductBasicDTO;
import com.hichina.main.back.hichinamainback.model.DTO.HichinaProductListDTO;
import com.hichina.main.back.hichinamainback.model.DTO.ProductAttributeTypeShortDTO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "hichinaProductMapper")
public interface HichinaProductMapper {

    @Select("select sku_id, product_type_name, t.product_type_id, product_name, product_content, created_time from hichina_product p inner join hichina_product_type t on p.product_type_id=t.product_type_id  where p.sku_group_id=#{skuGroupId} ")
    List<HichinaProductBasicDTO> findBySkuGroupId(String skuGroupId);

    @Select("select sku_group_id, sku_group_name, g.product_type_id , t.product_type_name, g.min_price, g.image_url  \n" +
            "from product_sku_group g inner join hichina_product_type t on t.product_type_id = g.product_type_id where g.enabled=1 order by g.created_date desc")
    List<HichinaProductListDTO> getAllProductList();

    @Select("select sku_group_id, sku_group_name, g.product_type_id , t.product_type_name, g.min_price, g.image_url  \n" +
            "from product_sku_group g inner join hichina_product_type t on t.product_type_id = g.product_type_id\n" +
            "where g.enabled=1 and g.product_type_id=#{productTypeId} order by g.created_date desc")
    List<HichinaProductListDTO> getAllProductListByProductTypeId(String productTypeId);

    @Select("select sku_group_id, sku_group_name, g.product_type_id , t.product_type_name, g.min_price, g.image_url  \n" +
            "from product_sku_group g inner join hichina_product_type t on t.product_type_id = g.product_type_id\n" +
            "where g.enabled=1 and g.product_type_id=#{productTypeId} and sku_group_name like CONCAT('%',CONCAT(#{query},'%')) order by g.created_date desc")
    List<HichinaProductListDTO> getAllProductListByQueryAndProductTypeId(String query, String productTypeId);

    @Select("select count(*) from (select sku_group_id, sku_group_name, g.product_type_id , t.product_type_name, g.min_price, g.image_url  \n" +
            "from product_sku_group g inner join hichina_product_type t on t.product_type_id = g.product_type_id\n" +
            "where g.enabled=1 and g.product_type_id=#{productTypeId} and sku_group_name like CONCAT('%',CONCAT(#{query},'%')))")
    Integer countAllProductListByQueryAndProductTypeId(String query, String productTypeId);

    @Select("select count(*) from (select sku_group_id, sku_group_name, g.product_type_id , t.product_type_name, g.min_price, g.image_url  \n" +
            "from product_sku_group g inner join hichina_product_type t on t.product_type_id = g.product_type_id\n" +
            "where g.enabled=1 and g.product_type_id=#{productTypeId}) aa")
    Integer countAllProductListByProductTypeId(String productTypeId);

    @Select("select sku_group_id, sku_group_name, g.product_type_id , t.product_type_name, g.min_price, g.image_url  \n" +
            "from product_sku_group g inner join hichina_product_type t on t.product_type_id = g.product_type_id\n" +
            "where g.enabled=1 and sku_group_name like CONCAT('%',CONCAT(#{query},'%')) order by g.created_date desc")
    List<HichinaProductListDTO> getAllProductListByQuery(String query);

    @Select("select count(*) from (select sku_group_id, sku_group_name, g.product_type_id , t.product_type_name, g.min_price, g.image_url  \n" +
            "from product_sku_group g inner join hichina_product_type t on t.product_type_id = g.product_type_id\n" +
            "where g.enabled=1 and sku_group_name like CONCAT('%',CONCAT(#{query},'%')) ) bb ")
    Integer countAllProductListByQuery(String query);


    @Select("select count(*) from (select sku_group_id, sku_group_name, g.product_type_id , t.product_type_name, g.min_price, g.image_url  \n" +
            "from product_sku_group g inner join hichina_product_type t on t.product_type_id = g.product_type_id where g.enabled=1) aa")
    Integer countAllProductList();

    @Select("select pm.attribute_id, a.attribute_name, a.data_type from hichina_product p inner join product_type_attribute_mapping pm on p.product_type_id=pm.product_type_id inner join product_attribute a on a.attribute_id=pm.attribute_id where p.sku_id=#{skuId}")
    List<ProductAttributeTypeShortDTO> findBindedAttributesBySkuId(String skuId);

}
