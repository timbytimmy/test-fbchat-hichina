package com.hichina.main.back.hichinamainback.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("OrderMeta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderMeta {
    private String orderId;

    private List<PassengerInfo> passengerInfo;

    private ContactInfo contactInfo;

    private String coupon;

    private ProductInfo productInfo;

    @Data
    private class PassengerInfo{
        private String surName;
        private String givenName;
        private String birthDay;
        private Integer gender;
        private String nationality;
        private String passportNo;
        private String passportExpireDate;
    }

    @Data
    private class ContactInfo{
        private String name;
        private String email;
        private String phone;
        private String address;
    }

    @Data
    private class ProductInfo{
        private String productTypeId;
        private String productName;
        private String productSkuId;
        private String productSkuGroupId;
        private String packageCategory;
        private String productTypeName;
        private Integer totalPrice;
        private Integer adultCount;
        private Integer childCount;
        private Integer infantCount;
        private String selectedDate;
    }

}
