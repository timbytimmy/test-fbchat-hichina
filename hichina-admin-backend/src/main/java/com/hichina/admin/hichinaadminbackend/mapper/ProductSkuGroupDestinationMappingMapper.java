package com.hichina.admin.hichinaadminbackend.mapper;

import com.hichina.admin.hichinaadminbackend.model.DTO.DestinationShortDTO;
import com.hichina.admin.hichinaadminbackend.model.ProductSkuGroupDestinationMapping;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "skuGroupDestinationMappingMapper")
public interface ProductSkuGroupDestinationMappingMapper {
    @Insert("INSERT INTO product_sku_group_destination_mapping(product_sku_group_id, destination_id) VALUES(#{skuGroupId}, #{destinationId})")
    void insert(ProductSkuGroupDestinationMapping productSkuGroupDestinationMapping);

    @Select("select d.destination_id, d.destination_name from product_sku_group_destination_mapping m inner join destination d on d.destination_id=m.destination_id where m.product_sku_group_id=#{skuGroupId}")
    List<DestinationShortDTO> getShortDestinationsBySkuGroupId(String skuGroupId);

    @Delete("Delete FROM product_sku_group_destination_mapping WHERE product_sku_group_id=#{skuGroupId} and destination_id=#{destinationId} ")
    void delete(String skuGroupId, String destinationId);
}
