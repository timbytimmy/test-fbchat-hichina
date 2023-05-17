package com.hichina.main.back.hichinamainback.model.DTO;

import com.hichina.main.back.hichinamainback.model.Order;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class OrderSummary {

    private String orderId;

    private String productName;

    private String productSkuGroupId;

    private String packageCategory;

    private Integer totalPrice;

    private String productTypeName;

    private Integer buyCount;

    private String selectedDate;

    private Order.OrderStatus orderStatus;

    private Date createdTime;

}
