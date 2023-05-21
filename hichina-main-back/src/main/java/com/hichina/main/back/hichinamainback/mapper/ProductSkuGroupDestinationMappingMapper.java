package com.hichina.main.back.hichinamainback.mapper;

import com.hichina.main.back.hichinamainback.model.DTO.HichinaProductListDTO;
import com.hichina.main.back.hichinamainback.model.ProductSkuGroupDestinationMapping;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "skuGroupDestinationMappingMapper")
public interface ProductSkuGroupDestinationMappingMapper {
    @Select("select g.sku_group_id , g.sku_group_name , g.product_type_id , t.product_type_name , g.min_price , g.image_url\n" +
            "from product_sku_group_destination_mapping m inner join destination d on d.destination_id=m.destination_id \n" +
            "inner join product_sku_group g on g.sku_group_id = m.product_sku_group_id \n" +
            "inner join hichina_product_type t on t.product_type_id = g.product_type_id \n" +
            "where d.destination_id=#{destinationId} and t.product_type_id='3a53caed-b788-4290-896d-7922532ad769' order by g.created_date desc")
    List<HichinaProductListDTO> findRelatedTourProductByDestinationId(String destinationId);
}
