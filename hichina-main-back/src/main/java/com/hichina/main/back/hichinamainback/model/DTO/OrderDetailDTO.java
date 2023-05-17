package com.hichina.main.back.hichinamainback.model.DTO;

import com.alibaba.fastjson.JSONObject;
import com.hichina.main.back.hichinamainback.model.Order;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class OrderDetailDTO {
    private String orderId;
    private String userId;
    private String productSkuId;
    private Date createdTime;
    private Date lastUpdateTime;
    private JSONObject meta;
    private Order.OrderStatus status;
    private String payingInfo;
    private String remark;
    private Integer price;

    public enum OrderStatus {
        SUBMITTED,
        PAID,
        DELETED,
        FINISHED;
    }
}
