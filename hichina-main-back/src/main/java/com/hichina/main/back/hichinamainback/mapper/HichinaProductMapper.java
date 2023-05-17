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

    @Select("select aa.sku_id, aa.sku_group_id , aa.sku_group_name, aa.product_type_id,aa.product_type_name, aa.price, aa.image_url from\n" +
            "(\n" +
            "select row_number() over (order by  pim.attribute_value desc) rn, g.created_date, g.sku_group_name ,p.product_type_id, hpt.product_type_name  , p.sku_id, g.sku_group_id, pim.attribute_value as price, psvam.attribute_value as image_url from product_sku_group g \n" +
            "inner join hichina_product p on p.sku_group_id = g.sku_group_id \n" +
            "inner join hichina_product_type hpt on hpt.product_type_id = p.product_type_id \n" +
            "inner join product_sku_int_attribute_mapping pim on pim.sku_id = p.sku_id  \n" +
            "inner join product_sku_varchar_attribute_mapping psvam on psvam.sku_id = p.sku_id \n" +
            "where g.enabled = 1 \n" +
            "and pim.attribute_id in ('e16df480-b17d-4442-91c2-d6c30d0d7ab0','e228b843-e054-41f8-91dd-19663460df54')\n" +
            "and psvam.attribute_id = '2dea54f4-9b9c-413a-8b3a-0caf273283d2'\t\n" +
            ") aa\n" +
            "inner join\n" +
            "(\n" +
            "select max(rn) as rn, sku_group_id from\n" +
            "(\n" +
            "select row_number() over (order by  pim.attribute_value desc) as rn,  g.sku_group_id, pim.attribute_value as price from product_sku_group g \n" +
            "inner join hichina_product p on p.sku_group_id = g.sku_group_id \n" +
            "inner join hichina_product_type hpt on hpt.product_type_id = p.product_type_id \n" +
            "inner join product_sku_int_attribute_mapping pim on pim.sku_id = p.sku_id  \n" +
            "inner join product_sku_varchar_attribute_mapping psvam on psvam.sku_id = p.sku_id \n" +
            "where g.enabled = 1 \n" +
            "and pim.attribute_id in ('e16df480-b17d-4442-91c2-d6c30d0d7ab0','e228b843-e054-41f8-91dd-19663460df54')\n" +
            "and psvam.attribute_id = '2dea54f4-9b9c-413a-8b3a-0caf273283d2'\t\n" +
            ") cc\n" +
            "group by sku_group_id\n" +
            ") bb\n" +
            "on aa.rn=bb.rn\n" +
            "order by aa.created_date desc")
    List<HichinaProductListDTO> getAllProductList();

    @Select("select aa.sku_id, aa.sku_group_id , aa.sku_group_name, aa.product_type_id,aa.product_type_name, aa.price, aa.image_url from\n" +
            "(\n" +
            "select row_number() over (order by  pim.attribute_value desc) rn, g.created_date, g.sku_group_name ,p.product_type_id, hpt.product_type_name  , p.sku_id, g.sku_group_id, pim.attribute_value as price, psvam.attribute_value as image_url from product_sku_group g \n" +
            "inner join hichina_product p on p.sku_group_id = g.sku_group_id \n" +
            "inner join hichina_product_type hpt on hpt.product_type_id = p.product_type_id \n" +
            "inner join product_sku_int_attribute_mapping pim on pim.sku_id = p.sku_id  \n" +
            "inner join product_sku_varchar_attribute_mapping psvam on psvam.sku_id = p.sku_id \n" +
            "where g.enabled = 1 \n" +
            "and pim.attribute_id in ('e16df480-b17d-4442-91c2-d6c30d0d7ab0','e228b843-e054-41f8-91dd-19663460df54')\n" +
            "and psvam.attribute_id = '2dea54f4-9b9c-413a-8b3a-0caf273283d2' and hpt.product_type_id=#{productTypeId}\t\n" +
            ") aa\n" +
            "inner join\n" +
            "(\n" +
            "select max(rn) as rn, sku_group_id from\n" +
            "(\n" +
            "select row_number() over (order by  pim.attribute_value desc) as rn,  g.sku_group_id, pim.attribute_value as price from product_sku_group g \n" +
            "inner join hichina_product p on p.sku_group_id = g.sku_group_id \n" +
            "inner join hichina_product_type hpt on hpt.product_type_id = p.product_type_id \n" +
            "inner join product_sku_int_attribute_mapping pim on pim.sku_id = p.sku_id  \n" +
            "inner join product_sku_varchar_attribute_mapping psvam on psvam.sku_id = p.sku_id \n" +
            "where g.enabled = 1 \n" +
            "and pim.attribute_id in ('e16df480-b17d-4442-91c2-d6c30d0d7ab0','e228b843-e054-41f8-91dd-19663460df54')\n" +
            "and psvam.attribute_id = '2dea54f4-9b9c-413a-8b3a-0caf273283d2' and hpt.product_type_id=#{productTypeId}\t\n" +
            ") cc\n" +
            "group by sku_group_id\n" +
            ") bb\n" +
            "on aa.rn=bb.rn\n" +
            "order by aa.created_date desc")
    List<HichinaProductListDTO> getAllProductListByProductTypeId(String productTypeId);

    @Select("select aa.sku_id, aa.sku_group_id , aa.sku_group_name, aa.product_type_id,aa.product_type_name, aa.price, aa.image_url from\n" +
            "(\n" +
            "select row_number() over (order by  pim.attribute_value desc) rn, g.created_date, g.sku_group_name ,p.product_type_id, hpt.product_type_name  , p.sku_id, g.sku_group_id, pim.attribute_value as price, psvam.attribute_value as image_url from product_sku_group g \n" +
            "inner join hichina_product p on p.sku_group_id = g.sku_group_id \n" +
            "inner join hichina_product_type hpt on hpt.product_type_id = p.product_type_id \n" +
            "inner join product_sku_int_attribute_mapping pim on pim.sku_id = p.sku_id  \n" +
            "inner join product_sku_varchar_attribute_mapping psvam on psvam.sku_id = p.sku_id \n" +
            "where g.enabled = 1 \n" +
            "and pim.attribute_id in ('e16df480-b17d-4442-91c2-d6c30d0d7ab0','e228b843-e054-41f8-91dd-19663460df54')\n" +
            "and psvam.attribute_id = '2dea54f4-9b9c-413a-8b3a-0caf273283d2' and g.sku_group_name like CONCAT('%',CONCAT(#{query},'%')) and hpt.product_type_id=#{productTypeId}\t\n" +
            ") aa\n" +
            "inner join\n" +
            "(\n" +
            "select max(rn) as rn, sku_group_id from\n" +
            "(\n" +
            "select row_number() over (order by  pim.attribute_value desc) as rn,  g.sku_group_id, pim.attribute_value as price from product_sku_group g \n" +
            "inner join hichina_product p on p.sku_group_id = g.sku_group_id \n" +
            "inner join hichina_product_type hpt on hpt.product_type_id = p.product_type_id \n" +
            "inner join product_sku_int_attribute_mapping pim on pim.sku_id = p.sku_id  \n" +
            "inner join product_sku_varchar_attribute_mapping psvam on psvam.sku_id = p.sku_id \n" +
            "where g.enabled = 1 \n" +
            "and pim.attribute_id in ('e16df480-b17d-4442-91c2-d6c30d0d7ab0','e228b843-e054-41f8-91dd-19663460df54')\n" +
            "and psvam.attribute_id = '2dea54f4-9b9c-413a-8b3a-0caf273283d2'\t\n" +
            ") cc\n" +
            "group by sku_group_id\n" +
            ") bb\n" +
            "on aa.rn=bb.rn\n" +
            "order by aa.created_date desc")
    List<HichinaProductListDTO> getAllProductListByQueryAndProductTypeId(String query, String productTypeId);

    @Select("select count(*) from\n" +
            "(\n" +
            "select aa.sku_id, aa.sku_group_id , aa.sku_group_name, aa.product_type_id,aa.product_type_name, aa.price, aa.image_url from\n" +
            "(\n" +
            "select row_number() over (order by  pim.attribute_value desc) rn, g.created_date, g.sku_group_name ,p.product_type_id, hpt.product_type_name  , p.sku_id, g.sku_group_id, pim.attribute_value as price, psvam.attribute_value as image_url from product_sku_group g \n" +
            "inner join hichina_product p on p.sku_group_id = g.sku_group_id \n" +
            "inner join hichina_product_type hpt on hpt.product_type_id = p.product_type_id \n" +
            "inner join product_sku_int_attribute_mapping pim on pim.sku_id = p.sku_id  \n" +
            "inner join product_sku_varchar_attribute_mapping psvam on psvam.sku_id = p.sku_id \n" +
            "where g.enabled = 1 \n" +
            "and pim.attribute_id in ('e16df480-b17d-4442-91c2-d6c30d0d7ab0','e228b843-e054-41f8-91dd-19663460df54')\n" +
            "and psvam.attribute_id = '2dea54f4-9b9c-413a-8b3a-0caf273283d2' and g.sku_group_name like CONCAT('%',CONCAT(#{query},'%')) and  hpt.product_type_id=#{productTypeId}\t\n" +
            ") aa\n" +
            "inner join\n" +
            "(\n" +
            "select max(rn) as rn, sku_group_id from\n" +
            "(\n" +
            "select row_number() over (order by  pim.attribute_value desc) as rn,  g.sku_group_id, pim.attribute_value as price from product_sku_group g \n" +
            "inner join hichina_product p on p.sku_group_id = g.sku_group_id \n" +
            "inner join hichina_product_type hpt on hpt.product_type_id = p.product_type_id \n" +
            "inner join product_sku_int_attribute_mapping pim on pim.sku_id = p.sku_id  \n" +
            "inner join product_sku_varchar_attribute_mapping psvam on psvam.sku_id = p.sku_id \n" +
            "where g.enabled = 1 \n" +
            "and pim.attribute_id in ('e16df480-b17d-4442-91c2-d6c30d0d7ab0','e228b843-e054-41f8-91dd-19663460df54')\n" +
            "and psvam.attribute_id = '2dea54f4-9b9c-413a-8b3a-0caf273283d2'\t\n" +
            ") cc\n" +
            "group by sku_group_id\n" +
            ") bb\n" +
            "on aa.rn=bb.rn\n" +
            ") ccd\n" +
            "\n")
    Integer countAllProductListByQueryAndProductTypeId(String query, String productTypeId);

    @Select("select count(*) from\n" +
            "(\n" +
            "select aa.sku_id, aa.sku_group_id , aa.sku_group_name, aa.product_type_id,aa.product_type_name, aa.price, aa.image_url from\n" +
            "(\n" +
            "select row_number() over (order by  pim.attribute_value desc) rn, g.created_date, g.sku_group_name ,p.product_type_id, hpt.product_type_name  , p.sku_id, g.sku_group_id, pim.attribute_value as price, psvam.attribute_value as image_url from product_sku_group g \n" +
            "inner join hichina_product p on p.sku_group_id = g.sku_group_id \n" +
            "inner join hichina_product_type hpt on hpt.product_type_id = p.product_type_id \n" +
            "inner join product_sku_int_attribute_mapping pim on pim.sku_id = p.sku_id  \n" +
            "inner join product_sku_varchar_attribute_mapping psvam on psvam.sku_id = p.sku_id \n" +
            "where g.enabled = 1 \n" +
            "and pim.attribute_id in ('e16df480-b17d-4442-91c2-d6c30d0d7ab0','e228b843-e054-41f8-91dd-19663460df54')\n" +
            "and psvam.attribute_id = '2dea54f4-9b9c-413a-8b3a-0caf273283d2' and hpt.product_type_id=#{productTypeId}\t\n" +
            ") aa\n" +
            "inner join\n" +
            "(\n" +
            "select max(rn) as rn, sku_group_id from\n" +
            "(\n" +
            "select row_number() over (order by  pim.attribute_value desc) as rn,  g.sku_group_id, pim.attribute_value as price from product_sku_group g \n" +
            "inner join hichina_product p on p.sku_group_id = g.sku_group_id \n" +
            "inner join hichina_product_type hpt on hpt.product_type_id = p.product_type_id \n" +
            "inner join product_sku_int_attribute_mapping pim on pim.sku_id = p.sku_id  \n" +
            "inner join product_sku_varchar_attribute_mapping psvam on psvam.sku_id = p.sku_id \n" +
            "where g.enabled = 1 \n" +
            "and pim.attribute_id in ('e16df480-b17d-4442-91c2-d6c30d0d7ab0','e228b843-e054-41f8-91dd-19663460df54')\n" +
            "and psvam.attribute_id = '2dea54f4-9b9c-413a-8b3a-0caf273283d2'\t\n" +
            ") cc\n" +
            "group by sku_group_id\n" +
            ") bb\n" +
            "on aa.rn=bb.rn\n" +
            ") ccd\n" +
            "\n")
    Integer countAllProductListByProductTypeId(String productTypeId);

    @Select("select aa.sku_id, aa.sku_group_id , aa.sku_group_name, aa.product_type_id,aa.product_type_name, aa.price, aa.image_url from\n" +
            "(\n" +
            "select row_number() over (order by  pim.attribute_value desc) rn, g.created_date, g.sku_group_name ,p.product_type_id, hpt.product_type_name  , p.sku_id, g.sku_group_id, pim.attribute_value as price, psvam.attribute_value as image_url from product_sku_group g \n" +
            "inner join hichina_product p on p.sku_group_id = g.sku_group_id \n" +
            "inner join hichina_product_type hpt on hpt.product_type_id = p.product_type_id \n" +
            "inner join product_sku_int_attribute_mapping pim on pim.sku_id = p.sku_id  \n" +
            "inner join product_sku_varchar_attribute_mapping psvam on psvam.sku_id = p.sku_id \n" +
            "where g.enabled = 1 \n" +
            "and pim.attribute_id in ('e16df480-b17d-4442-91c2-d6c30d0d7ab0','e228b843-e054-41f8-91dd-19663460df54')\n" +
            "and psvam.attribute_id = '2dea54f4-9b9c-413a-8b3a-0caf273283d2' and g.sku_group_name like CONCAT('%',CONCAT(#{query},'%'))\t\n" +
            ") aa\n" +
            "inner join\n" +
            "(\n" +
            "select max(rn) as rn, sku_group_id from\n" +
            "(\n" +
            "select row_number() over (order by  pim.attribute_value desc) as rn,  g.sku_group_id, pim.attribute_value as price from product_sku_group g \n" +
            "inner join hichina_product p on p.sku_group_id = g.sku_group_id \n" +
            "inner join hichina_product_type hpt on hpt.product_type_id = p.product_type_id \n" +
            "inner join product_sku_int_attribute_mapping pim on pim.sku_id = p.sku_id  \n" +
            "inner join product_sku_varchar_attribute_mapping psvam on psvam.sku_id = p.sku_id \n" +
            "where g.enabled = 1 \n" +
            "and pim.attribute_id in ('e16df480-b17d-4442-91c2-d6c30d0d7ab0','e228b843-e054-41f8-91dd-19663460df54')\n" +
            "and psvam.attribute_id = '2dea54f4-9b9c-413a-8b3a-0caf273283d2'\t\n" +
            ") cc\n" +
            "group by sku_group_id\n" +
            ") bb\n" +
            "on aa.rn=bb.rn\n" +
            "order by aa.created_date desc")
    List<HichinaProductListDTO> getAllProductListByQuery(String query);

    @Select("select count(*) from\n" +
            "(\n" +
            "select aa.sku_id, aa.sku_group_id , aa.sku_group_name, aa.product_type_id,aa.product_type_name, aa.price, aa.image_url from\n" +
            "(\n" +
            "select row_number() over (order by  pim.attribute_value desc) rn, g.created_date, g.sku_group_name ,p.product_type_id, hpt.product_type_name  , p.sku_id, g.sku_group_id, pim.attribute_value as price, psvam.attribute_value as image_url from product_sku_group g \n" +
            "inner join hichina_product p on p.sku_group_id = g.sku_group_id \n" +
            "inner join hichina_product_type hpt on hpt.product_type_id = p.product_type_id \n" +
            "inner join product_sku_int_attribute_mapping pim on pim.sku_id = p.sku_id  \n" +
            "inner join product_sku_varchar_attribute_mapping psvam on psvam.sku_id = p.sku_id \n" +
            "where g.enabled = 1 \n" +
            "and pim.attribute_id in ('e16df480-b17d-4442-91c2-d6c30d0d7ab0','e228b843-e054-41f8-91dd-19663460df54')\n" +
            "and psvam.attribute_id = '2dea54f4-9b9c-413a-8b3a-0caf273283d2' and g.sku_group_name like CONCAT('%',CONCAT(#{query},'%'))\t\n" +
            ") aa\n" +
            "inner join\n" +
            "(\n" +
            "select max(rn) as rn, sku_group_id from\n" +
            "(\n" +
            "select row_number() over (order by  pim.attribute_value desc) as rn,  g.sku_group_id, pim.attribute_value as price from product_sku_group g \n" +
            "inner join hichina_product p on p.sku_group_id = g.sku_group_id \n" +
            "inner join hichina_product_type hpt on hpt.product_type_id = p.product_type_id \n" +
            "inner join product_sku_int_attribute_mapping pim on pim.sku_id = p.sku_id  \n" +
            "inner join product_sku_varchar_attribute_mapping psvam on psvam.sku_id = p.sku_id \n" +
            "where g.enabled = 1 \n" +
            "and pim.attribute_id in ('e16df480-b17d-4442-91c2-d6c30d0d7ab0','e228b843-e054-41f8-91dd-19663460df54')\n" +
            "and psvam.attribute_id = '2dea54f4-9b9c-413a-8b3a-0caf273283d2'\t\n" +
            ") cc\n" +
            "group by sku_group_id\n" +
            ") bb\n" +
            "on aa.rn=bb.rn\n" +
            ") ccd\n" +
            "\n")
    Integer countAllProductListByQuery(String query);


    @Select("select count(*) from\n" +
            "(\n" +
            "select aa.sku_id, aa.sku_group_id , aa.sku_group_name, aa.product_type_id,aa.product_type_name, aa.price, aa.image_url from\n" +
            "(\n" +
            "select row_number() over (order by  pim.attribute_value desc) rn, g.created_date, g.sku_group_name ,p.product_type_id, hpt.product_type_name  , p.sku_id, g.sku_group_id, pim.attribute_value as price, psvam.attribute_value as image_url from product_sku_group g \n" +
            "inner join hichina_product p on p.sku_group_id = g.sku_group_id \n" +
            "inner join hichina_product_type hpt on hpt.product_type_id = p.product_type_id \n" +
            "inner join product_sku_int_attribute_mapping pim on pim.sku_id = p.sku_id  \n" +
            "inner join product_sku_varchar_attribute_mapping psvam on psvam.sku_id = p.sku_id \n" +
            "where g.enabled = 1 \n" +
            "and pim.attribute_id in ('e16df480-b17d-4442-91c2-d6c30d0d7ab0','e228b843-e054-41f8-91dd-19663460df54')\n" +
            "and psvam.attribute_id = '2dea54f4-9b9c-413a-8b3a-0caf273283d2'\t\n" +
            ") aa\n" +
            "inner join\n" +
            "(\n" +
            "select max(rn) as rn, sku_group_id from\n" +
            "(\n" +
            "select row_number() over (order by  pim.attribute_value desc) as rn,  g.sku_group_id, pim.attribute_value as price from product_sku_group g \n" +
            "inner join hichina_product p on p.sku_group_id = g.sku_group_id \n" +
            "inner join hichina_product_type hpt on hpt.product_type_id = p.product_type_id \n" +
            "inner join product_sku_int_attribute_mapping pim on pim.sku_id = p.sku_id  \n" +
            "inner join product_sku_varchar_attribute_mapping psvam on psvam.sku_id = p.sku_id \n" +
            "where g.enabled = 1 \n" +
            "and pim.attribute_id in ('e16df480-b17d-4442-91c2-d6c30d0d7ab0','e228b843-e054-41f8-91dd-19663460df54')\n" +
            "and psvam.attribute_id = '2dea54f4-9b9c-413a-8b3a-0caf273283d2'\t\n" +
            ") cc\n" +
            "group by sku_group_id\n" +
            ") bb\n" +
            "on aa.rn=bb.rn\n" +
            ") ccd")
    Integer countAllProductList();

    @Select("select pm.attribute_id, a.attribute_name, a.data_type from hichina_product p inner join product_type_attribute_mapping pm on p.product_type_id=pm.product_type_id inner join product_attribute a on a.attribute_id=pm.attribute_id where p.sku_id=#{skuId}")
    List<ProductAttributeTypeShortDTO> findBindedAttributesBySkuId(String skuId);

}
