package com.hichina.admin.hichinaadminbackend.mapper;

import com.hichina.admin.hichinaadminbackend.model.DTO.HichinaProductDTO;
import com.hichina.admin.hichinaadminbackend.model.DTO.ProductAttributeTypeShortDTO;
import com.hichina.admin.hichinaadminbackend.model.HichinaLine;
import com.hichina.admin.hichinaadminbackend.model.HichinaProduct;
import com.hichina.admin.hichinaadminbackend.model.ProductAttribute;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "hichinaProductMapper")
public interface HichinaProductMapper {

    @Insert("INSERT INTO hichina_product(sku_id, sku_group_id, product_type_id, product_name, product_content, created_time, created_by) VALUES(#{skuId}, #{skuGroupId}, #{productTypeId},#{productName},#{productContent}, #{createdTime}, #{createdBy})")
    @Options(keyProperty="sku_id")
    void insert(HichinaProduct product);

    @Delete("<script>" +
            "Delete FROM hichina_product WHERE sku_id in \n" +
            "    <foreach item='item' collection='toBeDel' open='(' separator=',' close=')'>\n" +
            "    #{item}" +
            "    </foreach>" +
            "</script>")
    void batchDelete(List<String> toBeDel);

    @Select("select pm.attribute_id, a.attribute_name, a.data_type from hichina_product p inner join product_type_attribute_mapping pm on p.product_type_id=pm.product_type_id inner join product_attribute a on a.attribute_id=pm.attribute_id where p.sku_id=#{skuId}")
    List<ProductAttributeTypeShortDTO> findBindedAttributesBySkuId(String skuId);

    @Select("<script> select sku_id, product_type_name, t.product_type_id, product_name, product_content, created_time from hichina_product p inner join hichina_product_type t on p.product_type_id=t.product_type_id  where <if test='mineOnly'> p.created_by=#{currentUser} and </if> p.product_type_id=#{productTypeId} and product_name like CONCAT('%',CONCAT(#{query},'%')) or product_content like CONCAT('%',CONCAT(#{query},'%')) order by p.created_time desc </script>")
    List<HichinaProductDTO> findByProductTypeIdAndQuery(String productTypeId, String query, boolean mineOnly, String currentUser);

    @Select("<script> select sku_id, product_type_name, t.product_type_id, product_name, product_content, created_time from hichina_product p inner join hichina_product_type t on p.product_type_id=t.product_type_id  <if test='mineOnly'> where p.created_by=#{currentUser} </if> order by p.created_time desc </script>")
    List<HichinaProductDTO> findAllProduct(boolean mineOnly, String currentUser);


    @Select("<script> select sku_id, product_type_name, t.product_type_id, product_name, product_content, created_time from hichina_product p inner join hichina_product_type t on p.product_type_id=t.product_type_id  where <if test='mineOnly'> p.created_by=#{currentUser} and </if> product_name like CONCAT('%',CONCAT(#{query},'%')) or product_content like CONCAT('%',CONCAT(#{query},'%')) order by p.created_time desc </script>")
    List<HichinaProductDTO> findByQuery(String query, boolean mineOnly, String currentUser);

    @Select("<script> select sku_id, product_type_name, product_name, product_content, created_time from hichina_product p inner join hichina_product_type t on p.product_type_id=t.product_type_id  where <if test='mineOnly'> p.created_by=#{currentUser} and </if> p.product_type_id=#{productTypeId} order by p.created_time desc </script>")
    List<HichinaProductDTO> findByProductTypeId(String productTypeId, boolean mineOnly, String currentUser);

    @Select("select sku_id, product_type_name, t.product_type_id, product_name, product_content, created_time from hichina_product p inner join hichina_product_type t on p.product_type_id=t.product_type_id  where p.sku_id=#{skuId} ")
    List<HichinaProductDTO> findBySkuId(String skuId);

    @Select("select * from hichina_product where sku_id=#{skuId}")
    List<HichinaProduct> findRawBySkuId(String skuId);


    @Select("<script> select count(*) from hichina_product where <if test='mineOnly'> created_by=#{currentUser} and </if> product_type_id=#{productTypeId} and product_name like CONCAT('%',CONCAT(#{query},'%')) or product_content like CONCAT('%',CONCAT(#{query},'%')) </script>")
    int countByProductTypeIdAndQuery(String productTypeId, String query, boolean mineOnly, String currentUser);

    @Select("<script> select count(*) from hichina_product where <if test='mineOnly'> p.created_by=#{currentUser} and </if> product_name like CONCAT('%',CONCAT(#{query},'%')) or product_content like CONCAT('%',CONCAT(#{query},'%')) </script>")
    int countByQuery(String query, boolean mineOnly, String currentUser);

    @Update("update hichina_product SET product_type_id=#{productTypeId}, sku_group_id=#{skuGroupId}, product_name=#{productName}, product_content=#{productContent}, created_time=#{createdTime} where sku_id=#{skuId}")
    void updateBySkuId(HichinaProduct product);

    @Select("<script> select count(*) from hichina_product where <if test='mineOnly'> created_by=#{currentUser} and </if> product_type_id=#{productTypeId} </script>")
    int countByProductTypeId(String productTypeId, boolean mineOnly, String currentUser);

    @Select("<script> select count(*) from hichina_product <if test='mineOnly'> where created_by=#{currentUser} </if> </script>")
    int count(boolean mineOnly, String currentUser);


}
