package com.hichina.admin.hichinaadminbackend.mapper;

import com.hichina.admin.hichinaadminbackend.model.DTO.SkuGroupStatsDTO;
import com.hichina.admin.hichinaadminbackend.model.HichinaLine;
import com.hichina.admin.hichinaadminbackend.model.ProductAttribute;
import com.hichina.admin.hichinaadminbackend.model.ProductSkuGroup;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "productSkuGroupMapper")
public interface ProductSkuGroupMapper {
    @Insert("INSERT INTO product_sku_group(sku_group_id, sku_group_name, product_type_id, created_date, enabled, image_url, min_price) VALUES(#{skuGroupId},#{skuGroupName}, #{productTypeId}, #{createdDate}, #{enabled}, #{imageUrl}, #{minPrice})")
    void insert(ProductSkuGroup productSkuGroup);

    @Update("update product_sku_group set min_price = (\n" +
            "select min(psiam.attribute_value) as min_price from hichina_product hp \n" +
            "inner join product_sku_int_attribute_mapping psiam on psiam.sku_id = hp.sku_id \n" +
            "where \n" +
            "hp.sku_group_id = #{skuGroupId} \n" +
            "and psiam.attribute_id in ('e16df480-b17d-4442-91c2-d6c30d0d7ab0', 'e228b843-e054-41f8-91dd-19663460df54')\n" +
            "), \n" +
            "image_url = (\n" +
            "select psiam.attribute_value as image_url from hichina_product hp \n" +
            "inner join product_sku_varchar_attribute_mapping psiam on psiam.sku_id = hp.sku_id \n" +
            "where \n" +
            "hp.sku_group_id = #{skuGroupId} \n" +
            "and psiam.attribute_id='2dea54f4-9b9c-413a-8b3a-0caf273283d2'\n" +
            "order by hp.created_time\n" +
            "limit 1\n" +
            ")\n" +
            "where sku_group_id = #{skuGroupId}")
    void updateMinPriceAndImage(String skuGroupId);

    @Select("<script>" +
            "select sku_group_id FROM hichina_product WHERE sku_id in \n" +
            "    <foreach item='item' collection='skuIds' open='(' separator=',' close=')'>\n" +
            "    #{item}" +
            "    </foreach>" +
            "</script>")
    List<String> findSkuGroupIdsBySkuIds(List<String> skuIds);

    @Update("update product_sku_group SET sku_group_name=#{skuGroupName}, product_type_id=#{productTypeId}, created_date=#{createdDate}, enabled=#{enabled}, image_url=#{imageUrl}, min_price=#{minPrice} where sku_group_id=#{skuGroupId}")
    void update(ProductSkuGroup group);

    @Select("select * from product_sku_group where sku_group_name=#{name} and product_type_id=#{productTypeId}")
    List<ProductSkuGroup> findByNameAndProductTypeId(String name, String productTypeId);

    @Select("select * from product_sku_group where sku_group_id=#{skuGroupId}")
    List<ProductSkuGroup> findBySkuGroupId(String skuGroupId);

    @Select("<script> select temp.sku_group_id, temp.sku_group_name, temp.product_type_id, t.product_type_name, temp.product_count, temp.enabled  \n" +
            "from \n" +
            "(\n" +
            "select g.sku_group_id , g.sku_group_name , g.product_type_id , g.enabled, g.created_date, count(p.sku_id) as product_count  \n" +
            "from product_sku_group g \n" +
            "inner join hichina_product p on g.sku_group_id = p.sku_group_id <if test='mineOnly'> where p.created_by=#{currentUser}</if> \n" +
            "group by g.sku_group_id , g.sku_group_name , g.product_type_id, g.enabled, g.created_date\n" +
            ") temp \n" +
            "inner join hichina_product_type t on t.product_type_id = temp.product_type_id \n" +
            "order by temp.created_date desc </script>")
    List<SkuGroupStatsDTO> findAllSkuGroupStats(boolean mineOnly, String currentUser);

    @Select("<script> select count(*) from\n" +
            "(\n" +
            "select temp.sku_group_id, temp.sku_group_name, temp.product_type_id, t.product_type_name, temp.product_count, temp.enabled  \n" +
            "from \n" +
            "(\n" +
            "select g.sku_group_id , g.sku_group_name , g.product_type_id , g.enabled, g.created_date, count(p.sku_id) as product_count  \n" +
            "from product_sku_group g \n" +
            "inner join hichina_product p on g.sku_group_id = p.sku_group_id <if test='mineOnly'>where p.created_by=#{currentUser}</if> \n" +
            "group by g.sku_group_id , g.sku_group_name , g.product_type_id, g.enabled, g.created_date\n" +
            ") temp \n" +
            "inner join hichina_product_type t on t.product_type_id = temp.product_type_id \n" +
            ") cc </script>")
    Integer countAllSkuGroupStats(boolean mineOnly, String currentUser);

    @Select("<script> select temp.sku_group_id, temp.sku_group_name, temp.product_type_id, t.product_type_name, temp.product_count, temp.enabled  \n" +
            "from \n" +
            "(\n" +
            "select g.sku_group_id , g.sku_group_name , g.product_type_id , g.enabled, g.created_date, count(p.sku_id) as product_count  \n" +
            "from product_sku_group g \n" +
            "inner join hichina_product p on g.sku_group_id = p.sku_group_id <if test='mineOnly'>where p.created_by=#{currentUser}</if> \n" +
            "group by g.sku_group_id , g.sku_group_name , g.product_type_id, g.enabled, g.created_date\n" +
            ") temp \n" +
            "inner join hichina_product_type t on t.product_type_id = temp.product_type_id \n" +
            "where temp.sku_group_name like  CONCAT('%',CONCAT(#{query},'%')) order by temp.created_date desc </script>")
    List<SkuGroupStatsDTO> findSkuGroupStatsByQuery(String query, boolean mineOnly, String currentUser);

    @Select("<script> select count(*) from\n" +
            "(\n" +
            "select temp.sku_group_id, temp.sku_group_name, temp.product_type_id, t.product_type_name, temp.product_count, temp.enabled  \n" +
            "from \n" +
            "(\n" +
            "select g.sku_group_id , g.sku_group_name , g.product_type_id , g.enabled, g.created_date, count(p.sku_id) as product_count  \n" +
            "from product_sku_group g \n" +
            "inner join hichina_product p on g.sku_group_id = p.sku_group_id <if test='mineOnly'>where p.created_by=#{currentUser} </if> \n" +
            "group by g.sku_group_id , g.sku_group_name , g.product_type_id, g.enabled, g.created_date\n" +
            ") temp \n" +
            "inner join hichina_product_type t on t.product_type_id = temp.product_type_id \n" +
            "where temp.sku_group_name like CONCAT('%',CONCAT(#{query},'%'))\n" +
            ") cc </script>")
    Integer countSkuGroupStatsByQuery(String query, boolean mineOnly, String currentUser);

    @Delete("delete from product_sku_group where sku_group_id not in (select distinct sku_group_id from hichina_product)")
    void deleteUnreferenced();
}
