package com.hichina.admin.hichinaadminbackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Order {
    private String orderId;
    private String userId;
    private String productSkuId;
    private Date createdTime;
    private Date lastUpdateTime;
    private String meta;
    private OrderStatus status;
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
