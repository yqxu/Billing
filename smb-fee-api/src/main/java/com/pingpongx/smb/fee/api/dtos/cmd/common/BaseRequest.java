package com.pingpongx.smb.fee.api.dtos.cmd.common;

import com.pingpongx.smb.component.enums.BuEnum;
import com.pingpongx.smb.fee.api.dtos.cmd.trade.OrderInfo;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BaseRequest {
    private static final long serialVersionUID = 8061568762858026972L;

    /***
     * 计费时间
     */
    Long billingTime;
    /***
     * 计费节点
     */
    String costNodeCode;
    /***
     * Did buClientId 具体数据
     */
    String subject;
    /***
     * Did / accid / Nid / buClientId
     * @see com.pingpongx.smb.fee.api.enums.SubjectType
     */
    String subjectType;
    /***
     * 汇率列表，目标货种：原币种  目标币种：美元  原币种：美元
     */
    Map<String, RateInfo> fxRate;

    OrderInfo orderInfo;
    /**
     * @see BuEnum
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

    public String identify() {
        String split = ":";
        StringBuilder builder = new StringBuilder();
        builder.append(costNodeCode).append(split);
        builder.append(orderInfo.identify()).append(split);
        return builder.toString();
    }


    public String valid(){
        if (this.costNodeCode == null){
            return "cost node code can't be null.";
        }
        if (this.billingTime == null){
            return "billingTime can't be null.";
        }
        if (orderInfo ==null){
            return "order info can't be null.";
        }
        if (sourceApp ==null){
            return "sourceApp info can't be null.";
        }
        return orderInfo.valid();
    }
}
