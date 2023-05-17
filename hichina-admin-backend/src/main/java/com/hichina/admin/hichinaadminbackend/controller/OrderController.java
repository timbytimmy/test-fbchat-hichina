package com.hichina.admin.hichinaadminbackend.controller;

import com.github.pagehelper.PageHelper;
import com.hichina.admin.hichinaadminbackend.mapper.OrderMapper;
import com.hichina.admin.hichinaadminbackend.model.DTO.HichinaResponse;
import com.hichina.admin.hichinaadminbackend.model.DTO.OrderDetailDTO;
import com.hichina.admin.hichinaadminbackend.model.DTO.OrderListDTO;
import com.hichina.admin.hichinaadminbackend.model.DTO.PaginationWrapper;
import com.hichina.admin.hichinaadminbackend.model.Destination;
import com.hichina.admin.hichinaadminbackend.model.Order;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    @Autowired
    private OrderMapper orderMapper;

    @GetMapping("/{orderId}")
    public HichinaResponse getOrderById(@PathVariable("orderId") String orderId) throws UnsupportedEncodingException {
        HichinaResponse ret = new HichinaResponse();

        List<OrderDetailDTO> orderDetailDTOS = orderMapper.findOrderDetailById(orderId);

        if(orderDetailDTOS.isEmpty()){
            throw new RuntimeException("Cannot get any order by id: "+ orderId);
        }

        OrderDetailDTO orderDetail = orderDetailDTOS.get(0);
        orderDetail.setMeta(Base64.getEncoder().encodeToString(orderDetail.getMeta().getBytes("UTF-8")));
        ret.setData(orderDetail);
        ret.setMessage("successfully get order detail by id:"+orderId);
        ret.setOk(true);
        return ret;
    }

    @GetMapping
    public HichinaResponse getOrders(@RequestParam(value = "page", required = true) Integer page,
                                           @RequestParam(value = "pageSize", required = true) Integer size,
                                           @RequestParam(value = "query") String query){
        HichinaResponse ret = new HichinaResponse();
        if(page>0){
            PageHelper.startPage(page,size);
        }

        List<OrderListDTO> rawData;
        int cnt = 0;
        if(!StringUtils.isEmpty(query)){
            rawData = orderMapper.findOrdersByQuery(query);
            cnt = orderMapper.countByQuery(query);
        }else{
            rawData = orderMapper.findAllOrders();
            cnt = orderMapper.count();
        }

        ret.setMessage("Successfully got all orders");

        PaginationWrapper paginationWrapper = new PaginationWrapper();
        paginationWrapper.setData(rawData);
        paginationWrapper.setPageSize(size);
        paginationWrapper.setCurrentPage(page);
        paginationWrapper.setTotal(cnt);

        ret.setData(paginationWrapper);
        ret.setOk(true);

        return ret;
    }
}
