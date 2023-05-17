package com.hichina.admin.hichinaadminbackend.mapper;

import com.hichina.admin.hichinaadminbackend.model.DTO.OrderDetailDTO;
import com.hichina.admin.hichinaadminbackend.model.DTO.OrderListDTO;
import com.hichina.admin.hichinaadminbackend.model.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "orderMapper")
public interface OrderMapper {

//            status
    @Select("select count(*) from `order`")
    int count();

    @Select("select count(*) from `order` o inner join hichina_product p on p.sku_id=o.product_sku_id inner join hichina_product_type pt on pt.product_type_id=p.product_type_id where p.product_name like CONCAT('%',CONCAT(#{query},'%')) or o.user_id like CONCAT('%',CONCAT(#{query},'%')) order by o.created_time desc")
    int countByQuery(String query);


    @Select("select o.order_id, o.user_id, p.product_name, p.sku_id, p.product_type_id, pt.product_type_name, o.created_time, o.status, o.price from `order` o inner join hichina_product p on p.sku_id=o.product_sku_id inner join hichina_product_type pt on pt.product_type_id=p.product_type_id order by o.created_time desc")
    List<OrderListDTO> findAllOrders();

    @Select("select o.order_id, o.user_id, p.product_name, p.sku_id, p.product_type_id, pt.product_type_name, o.created_time, o.status, o.price from `order` o inner join hichina_product p on p.sku_id=o.product_sku_id inner join hichina_product_type pt on pt.product_type_id=p.product_type_id where p.product_name like CONCAT('%',CONCAT(#{query},'%')) or o.user_id like CONCAT('%',CONCAT(#{query},'%')) order by o.created_time desc")
    List<OrderListDTO> findOrdersByQuery(String query);

    @Select("select o.order_id, o.user_id, p.product_name, p.sku_id, p.product_type_id, pt.product_type_name, o.created_time, o.last_update_time, o.status, o.price, o.meta, o.paying_info, o.remark from `order` o inner join hichina_product p on p.sku_id=o.product_sku_id inner join hichina_product_type pt on pt.product_type_id=p.product_type_id where o.order_id=#{orderId} order by o.created_time desc")
    List<OrderDetailDTO> findOrderDetailById(String orderId);
}
