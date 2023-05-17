package com.hichina.main.back.hichinamainback.mapper;

import com.hichina.main.back.hichinamainback.model.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
@Component(value = "orderMapper")
public interface OrderMapper {
    @Insert("INSERT INTO `order` (order_id, user_id, product_sku_id, created_time, last_update_time, meta, status, paying_info, remark, price) VALUES(#{orderId},#{userId}, #{productSkuId}, #{createdTime}, #{lastUpdateTime}, #{meta}, #{status}, #{payingInfo}, #{remark}, #{price})")
    void insert(Order order);

    @Select("select * from `order` where order_id=#{orderId}")
    List<Order> findByOrderId(String orderId);

    @Select("select * from `order` where user_id=#{userId} order by created_time desc")
    List<Order> findByUserId(String userId);

    @Select("select * from `order` where order_id like CONCAT('%',CONCAT(#{opfx})) and user_id like CONCAT('%',CONCAT(#{upfx})) order by created_time desc")
    List<Order> findByOrderIdPostFixAndUserIdPostfix(String opfx, String upfx);

    @Select("select count(*) from `order` where user_id=#{userId}")
    Integer countByUserId(String userId);

    @Select("select * from `order` where user_id=#{userId} and meta like CONCAT('%',CONCAT(#{query},'%')) order by created_time desc")
    List<Order> findByUserIdAndQuery(String userId, String query);

    @Select("select count(*) from `order` where user_id=#{userId} and meta like CONCAT('%',CONCAT(#{query},'%'))")
    Integer countByUserIdAndQuery(String userId, String query);

    @Update("update `order` SET user_id=#{userId}, product_sku_id=#{productSkuId}, created_time=#{createdTime}, last_update_time=#{lastUpdateTime}, meta=#{meta}, status=#{status}, paying_info=#{payingInfo}, price=#{price}, remark=#{remark} where order_id=#{orderId}")
    void update(Order order);
}
