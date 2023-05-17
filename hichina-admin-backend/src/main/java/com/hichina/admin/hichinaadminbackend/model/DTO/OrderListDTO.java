package com.hichina.admin.hichinaadminbackend.model.DTO;

import com.hichina.admin.hichinaadminbackend.model.Order;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class OrderListDTO {
    private String orderId;
    private String userId;
    private String productName;
    private String skuId;
    private String productTypeId;
    private String productTypeName;
    private Date createdTime;
    private Order.OrderStatus status;
    private Integer price;
}
