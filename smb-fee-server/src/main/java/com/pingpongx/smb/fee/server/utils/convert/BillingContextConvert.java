package com.pingpongx.smb.fee.server.utils.convert;

import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.pingpongx.smb.fee.api.dtos.cmd.BillingRequest;
import com.pingpongx.smb.fee.api.dtos.cmd.CouponInfo;
import com.pingpongx.smb.fee.api.dtos.cmd.OrderInfo;
import com.pingpongx.smb.fee.api.dtos.resp.CostItemResult;
import com.pingpongx.smb.fee.api.dtos.resp.CouponResult;
import com.pingpongx.smb.fee.dal.dataobject.BillingProcessDo;
import com.pingpongx.smb.fee.dal.dataobject.BillingRequestDo;
import com.pingpongx.smb.fee.domain.module.CostItem;
import com.pingpongx.smb.fee.domain.runtime.BillingContext;
import com.pingpongx.smb.fee.server.utils.ConvertUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class BillingContextConvert {
    public static BillingProcessDo toDo(BillingContext context) {
        return ConvertUtil.toDo(context, BillingProcessDo.class, (one) -> {
            one.setBizOrderId(context.getRequest().getOrder().getBizOrderId());
            one.setBizOrderType(context.getRequest().getOrder().getBizOrderType());
            one.setRequestRepeatKey(context.getRequest().identify());
            if (context.getRequest()!=null){
                one.setRequest(JSON.toJSONString(context.getRequest()));
            }
            if (context.getParams()!=null){
                one.setParams(JSON.toJSONString(context.getParams()));
            }
            if (context.getFeeResult()!=null){
                one.setFeeResult(JSON.toJSONString(context.getFeeResult()));
            }
            if (context.getMatchedCostItem()!=null){
                one.setMatchedCostItem(JSON.toJSONString(context.getMatchedCostItem()));
            }
            if (context.getExpense()!=null){
                one.setExpense(JSON.toJSONString(context.getExpense()));
            }
            if (context.getCache()!=null){
                one.setCache(JSON.toJSONString(context.getCache()));
            }
        });
    }

    public static BillingContext toContext(BillingProcessDo dbO) {
        return ConvertUtil.toDto(dbO, BillingContext.class, (one) -> {
            if (!StringUtils.isBlank(dbO.getRequest())){
                one.setRequest(JSON.parseObject(dbO.getRequest(),BillingRequest.class));
            }
            if (!StringUtils.isBlank(dbO.getParams())){
                one.setParams(JSON.parseObject(dbO.getParams(), Map.class));
            }
            if (!StringUtils.isBlank(dbO.getFeeResult())){
                one.setFeeResult(JSON.parseArray(dbO.getFeeResult(), CostItemResult.class));
            }
            if (!StringUtils.isBlank(dbO.getMatchedCostItem())){
                one.setMatchedCostItem(JSON.parseArray(dbO.getMatchedCostItem(), CostItem.class));
            }
            if (!StringUtils.isBlank(dbO.getExpense())){
                one.setExpense(JSON.parseArray(dbO.getExpense(), CouponResult.class));
            }
        });
    }
}
