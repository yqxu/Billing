package com.pingpongx.smb.fee.api.dtos.cmd;

import io.vavr.collection.Tree;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BillingRequest {
    /***
     * 计费时间
     */
    Long billingTime;
    /***
     * Did buClientId 具体数据
     */
    String subject;
    /***
     * Did / accid / Nid / buClientId
     */
    String subjectType;
    /***
     * 汇率列表，目标货种：原币种  目标币种：美元  原币种：美元
     */
    Map<String, BigDecimal> fxRate;
    /**
     * 用于幂等
     */
    Map<String, BigDecimal> fxRateId;
    OrderInfo order;
    /**
     * 来源系统 B2B FM Dispatch Common
     */
    String bizLine;
    /**
     * 来源应用
     */
    String sourceApp;
    /**
     * 优惠券
     */
    List<CouponInfo> couponList;

}
