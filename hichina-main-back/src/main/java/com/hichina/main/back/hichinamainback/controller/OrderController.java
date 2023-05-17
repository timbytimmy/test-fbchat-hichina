package com.hichina.main.back.hichinamainback.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.hichina.main.back.hichinamainback.mapper.OrderMapper;
import com.hichina.main.back.hichinamainback.mapper.UserMapper;
import com.hichina.main.back.hichinamainback.model.DTO.*;
import com.hichina.main.back.hichinamainback.model.Order;
import com.hichina.main.back.hichinamainback.model.User;
import com.hichina.main.back.hichinamainback.model.mongo.OrderMeta;
import com.hichina.main.back.hichinamainback.repository.OrderMetaRepository;
import com.hichina.main.back.hichinamainback.service.AliPayService;
import com.hichina.main.back.hichinamainback.service.WechatPayService;
import com.hichina.main.back.hichinamainback.utils.UserUtil;
import com.wechat.pay.java.service.payments.model.Transaction;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderMetaRepository orderMetaRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AliPayService aliPayService;

    @Autowired
    private WechatPayService wechatPayService;

    @GetMapping("/wechatpaystatus/{orderId}")
    public String getWechatpayStatusByOrderId(@PathVariable("orderId") String orderId){
        User user = UserUtil.getUserByCurrentPrincipal(userMapper, UserController.currentUser());
        String outTradeNo = getLastNsubstr(orderId, 16)+getLastNsubstr(user.getUserId(), 16);
        LOG.info("===checking outTradeNo wechat payment status: "+ outTradeNo);
        Transaction paymentStatus = wechatPayService.checkPaymentStatus(outTradeNo);
        if("SUCCESS".equals(paymentStatus.getTradeState().toString())){
            List<Order> orders = orderMapper.findByOrderId(orderId);
            if(orders.isEmpty()){
                throw new RuntimeException("===getWechatpayStatusByOrderId, Cannot find order by :"+ orderId);
            }
            Order order = orders.get(0);
            if(StringUtils.isEmpty(order.getPayingInfo())){
                order.setPayingInfo(paymentStatus.toString());
            }
            order.setStatus(Order.OrderStatus.PAID);
            orderMapper.update(order);
        }
        return paymentStatus.getTradeState().toString();
    }

    @PutMapping("/succeed/{orderId}")
    public HichinaResponse succeedPay4Order(@PathVariable("orderId") String orderId, @RequestBody PayInfoRequest request){
        HichinaResponse ret = new HichinaResponse();

        List<Order> orders =  orderMapper.findByOrderId(orderId);
        if(orders.isEmpty()){
            throw new RuntimeException("No such order: "+orderId);
        }
        Order order = orders.get(0);
        order.setStatus(Order.OrderStatus.PAID);
        if(StringUtils.isEmpty(order.getPayingInfo())){
            order.setPayingInfo(String.format("%s, outTradeNo: %s", request.getMethod(), request.getTradeNo()));
        }
        orderMapper.update(order);
        ret.setOk(true);
        ret.setData(orderId);
        ret.setMessage("成功更新order");

        return ret;
    }

    @PostMapping
    public HichinaResponse creatOrder(@RequestBody OrderCreationDTO request) throws UnsupportedEncodingException {

        HichinaResponse ret = new HichinaResponse();
        User user = UserUtil.getUserByCurrentPrincipal(userMapper, UserController.currentUser());
        Order order = new Order();
        Date currentTimestamp = new Date();
        order.setCreatedTime(currentTimestamp);
        order.setLastUpdateTime(currentTimestamp);
        order.setMeta(request.getMeta());
        order.setOrderId(java.util.UUID.randomUUID().toString());
        order.setStatus(Order.OrderStatus.valueOf(request.getStatus()));
        order.setUserId(user.getUserId());
        order.setProductSkuId(request.getSkuId());
        order.setPrice(request.getPrice());

        // insert order meta into mongo
        Gson g = new Gson();
        OrderMeta orderMeta = g.fromJson(request.getMeta(), OrderMeta.class);
        orderMeta.setOrderId(order.getOrderId());

        orderMapper.insert(order);
        orderMetaRepository.insert(orderMeta);
        PayDTO payDTO;
        if("alipay".equals(request.getPayMethod())){
            payDTO = aliPayService.getPayForm(order.getOrderId(), order.getProductSkuId(), order.getPrice());
        }else if("wechatpay".equals(request.getPayMethod())){
            String generatedOutTradeNo = getLastNsubstr(order.getOrderId(),16)+getLastNsubstr(user.getUserId(), 16);
            LOG.info("===generatedOutTradeNo: " + generatedOutTradeNo);
            payDTO = wechatPayService.getPayForm(generatedOutTradeNo,order.getOrderId(), order.getProductSkuId(), order.getPrice());
        }else{
            LOG.error("===Not supported pay method:"+request.getPayMethod());
            throw new RuntimeException("not supported pay method");
        }

        ret.setData(payDTO);
        ret.setOk(true);
        ret.setMessage("Succeeded create order");
        return ret;
    }

    @GetMapping("/orderDetail/{orderId}")
    public HichinaResponse getOrderDetail(@PathVariable("orderId") String orderId){
        HichinaResponse ret = new HichinaResponse();
        List<Order> orders = orderMapper.findByOrderId(orderId);
        if(orders.isEmpty()){
            throw new RuntimeException("Order not found: "+ orderId);
        }
        Order order = orders.get(0);
        ret.setMessage("Succeeded getting order detail: "+ orderId);
        ret.setOk(true);
        ret.setData(toOrderDetail(order));
        return ret;
    }

    private OrderDetailDTO toOrderDetail(Order order){
        OrderDetailDTO ret = new OrderDetailDTO();

        ret.setCreatedTime(order.getCreatedTime());
        ret.setPrice(order.getPrice());
        ret.setMeta(JSON.parseObject(order.getMeta()));
        ret.setPayingInfo(order.getPayingInfo());
        ret.setStatus(order.getStatus());
        ret.setUserId(order.getUserId());
        ret.setRemark(order.getRemark());
        ret.setOrderId(order.getOrderId());
        ret.setLastUpdateTime(order.getLastUpdateTime());
        ret.setProductSkuId(order.getProductSkuId());

        return ret;
    }

    @GetMapping("/myorders")
    public HichinaResponse getMyOrders(@RequestParam(value = "page", required = true) Integer page,
                                       @RequestParam(value = "pageSize", required = true) Integer size,
                                       @RequestParam(value = "query") String query){
        HichinaResponse ret = new HichinaResponse();
        User user = UserUtil.getUserByCurrentPrincipal(userMapper, UserController.currentUser());

        if(page>0){
            PageHelper.startPage(page,size);
        }

        List<Order> orders;
        int cnt = 0;
        if(StringUtils.isEmpty(query)){
            orders = orderMapper.findByUserId(user.getUserId());
            cnt = orderMapper.countByUserId(user.getUserId());
        }else{
            orders = orderMapper.findByUserIdAndQuery(user.getUserId(), query);
            cnt = orderMapper.countByUserIdAndQuery(user.getUserId(), query);
        }

        List<OrderSummary> orderSummaries = orders.stream().map(r->{return convert2OrderSummary(r);}).collect(Collectors.toList());

        PaginationWrapper paginationWrapper = new PaginationWrapper();
        paginationWrapper.setData(orderSummaries);
        paginationWrapper.setPageSize(size);
        paginationWrapper.setCurrentPage(page);
        paginationWrapper.setTotal(cnt);

        ret.setData(paginationWrapper);
        ret.setOk(true);
        ret.setMessage("Successfully get my order summary");

        return ret;
    }

    private OrderSummary convert2OrderSummary(Order originOrder){
        OrderSummary ret = new OrderSummary();
        String orderMetaStr = originOrder.getMeta();
        JSONObject jsonObject = JSON.parseObject(orderMetaStr);
        JSONObject productInfo = (JSONObject) jsonObject.get("productInfo");
        if(productInfo!=null){
            if(productInfo.get("buyCount")!=null){
                ret.setBuyCount(Integer.parseInt(productInfo.get("buyCount").toString()));
            }
            ret.setProductName((String)productInfo.get("productName"));
            ret.setPackageCategory((String)productInfo.get("packageCategory"));
            ret.setProductTypeName((String)productInfo.get("productTypeName"));
            ret.setProductSkuGroupId((String)productInfo.get("productSkuGroupId"));
            ret.setSelectedDate((String)productInfo.get("selectedDate"));
        }
        ret.setOrderStatus(originOrder.getStatus());
        ret.setTotalPrice(originOrder.getPrice());
        ret.setOrderId(originOrder.getOrderId());
        ret.setCreatedTime(originOrder.getCreatedTime());
        return ret;
    }

    private String getLastNsubstr(String origin, int n){
        if(origin.length()<n){
            throw new RuntimeException("Not valid input for getLastNsubstr");
        }
        return origin.substring(origin.length() - n);
    }
}
