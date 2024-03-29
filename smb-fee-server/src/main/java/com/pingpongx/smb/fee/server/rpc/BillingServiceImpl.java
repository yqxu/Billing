package com.pingpongx.smb.fee.server.rpc;

import com.alibaba.fastjson2.JSON;
import com.pingpongx.flowmore.cloud.base.server.annotation.Internal;
import com.pingpongx.flowmore.cloud.base.server.constants.RoleRegister;
import com.pingpongx.smb.fee.api.dtos.cmd.common.PayeeInfo;
import com.pingpongx.smb.fee.api.dtos.cmd.common.PayerInfo;
import com.pingpongx.smb.fee.api.dtos.cmd.common.RateInfo;
import com.pingpongx.smb.fee.api.dtos.cmd.trade.BatchCmd;
import com.pingpongx.smb.fee.api.dtos.cmd.trade.BillingRequest;
import com.pingpongx.smb.fee.api.dtos.cmd.trade.OrderInfo;
import com.pingpongx.smb.fee.api.dtos.resp.Bill;
import com.pingpongx.smb.fee.api.dtos.resp.BillList;
import com.pingpongx.smb.fee.api.enums.FeePayer;
import com.pingpongx.smb.fee.api.feign.BillingServiceFeign;
import com.pingpongx.smb.fee.dal.dataobject.BillingContextDo;
import com.pingpongx.smb.fee.dal.dataobject.BillingRequestDo;
import com.pingpongx.smb.fee.dal.dataobject.RepeatDo;
import com.pingpongx.smb.fee.dal.repository.BillingContextRepository;
import com.pingpongx.smb.fee.dal.repository.BillingRequestRepository;
import com.pingpongx.smb.fee.dal.repository.RepeatRepository;
import com.pingpongx.smb.fee.dependency.convert.BillingRequestConvert;
import com.pingpongx.smb.fee.domain.convert.runtime.BillingContextConvert;
import com.pingpongx.smb.fee.domain.module.Request;
import com.pingpongx.smb.fee.domain.module.event.BillingRequestReceived;
import com.pingpongx.smb.fee.domain.module.event.BillingStage;
import com.pingpongx.smb.fee.domain.module.express.AXpB;
import com.pingpongx.smb.fee.domain.module.express.Expr;
import com.pingpongx.smb.fee.domain.module.express.Min;
import com.pingpongx.smb.fee.domain.module.express.Sum;
import com.pingpongx.smb.fee.domain.runtime.BillingContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;


@Api(tags = "计费中心-new")
@RestController
@RequestMapping(value = BillingServiceFeign.BASE_PATH)
@Slf4j
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingServiceFeign {

    private final RepeatRepository repeatRepository;
    private final BillingRequestRepository billingRequestRepository;
    private final BillingContextRepository contextRepository;
    private final TransactionTemplate txTemplate;
    private final ApplicationContext springContext;
    private final BillingContextConvert billingContextConvert;

    public static void main(String args[]) {
        AXpB val = new AXpB();
        val.setA(BigDecimal.ONE);
        val.setX("val");
        val.setB(BigDecimal.ZERO);

        AXpB fee = new AXpB();
        fee.setA(BigDecimal.ONE);
        fee.setX("totalFee");
        fee.setB(BigDecimal.ZERO);

        AXpB amount = new AXpB();
        amount.setA(BigDecimal.ONE);
        amount.setX("amount");
        amount.setB(BigDecimal.ZERO);


        List<Expr> sumList = new ArrayList<>();
        sumList.add(fee);
        sumList.add(amount);
        Sum sum = new Sum();
        sum.setList(sumList);

        List<Expr> minList = new ArrayList<>();
        minList.add(val);
        minList.add(sum);
        Min expr = new Min();
        expr.setList(minList);

        String text = JSON.toJSONString(expr.toDto());

        BillingRequest request = new BillingRequest();
        request.setBillingTime(System.currentTimeMillis());
        request.setBizLine("FM");
        request.setCostNodeCode("ClientTransferStart");
        request.setSourceApp("FMPayout");

        Map<String, RateInfo> currencyMap = new HashMap<>();
        currencyMap.put("CNY_USD", RateInfo.of("CNY", "USD", "0.7", 123123L));
        request.setFxRate(currencyMap);

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setBizOrderId("TestBizOrderId");
        orderInfo.setBizOrderType("B2B");

        PayeeInfo payeeInfo = new PayeeInfo();
        payeeInfo.setPayeeAccountNo("X1000101010100101");
        payeeInfo.setPayeeName("被付款方名");
        payeeInfo.setBankName("硅谷破产银行");
        payeeInfo.setClientIdType("DID");
        payeeInfo.setClientId("D1230912380912830");
        orderInfo.setPayeeInfo(payeeInfo);
        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setClientId("D12038901283102938");
        payerInfo.setClientIdType("DID");
        payerInfo.setPayerName("付款方名");
        payerInfo.setCertificateNumber("大壮");
        payerInfo.setReservedPhone("15015011501");
        orderInfo.setPayerInfo(payerInfo);

        orderInfo.setAmount(new BigDecimal("11111"));
        orderInfo.setSourceCurrency("USD");
        orderInfo.setTargetCurrency("CNY");
        orderInfo.setSubject("D12038901283102938");
        orderInfo.setSubjectType("DID");
        orderInfo.setFeePayer(FeePayer.OrderPayee.name());
        orderInfo.setBizOrderType("Transfer");
        orderInfo.setBizOrderId("T0192091203121");
        request.setOrderInfo(orderInfo);
        request.setSubject(orderInfo.getSubject());
        request.setSubjectType(orderInfo.getSubjectType());
        String jsonStr = JSON.toJSONString(request);
        log.info(jsonStr);
        BillingRequest parsed = JSON.parseObject(jsonStr, BillingRequest.class);
//{"billingTime":1678930994261,"bizLine":"FM","costNodeCode":"ClientTransferStart","couponList":[],"fxRate":{"CNY_USD":0.6999999999999999555910790149937383830547332763671875},"fxRateId":"FX213123123","orderInfo":{"amount":11111,"bizOrderId":"T0192091203121","bizOrderType":"Transfer","feePayer":"Payee","payeeInfo":{"bankName":"硅谷破产银行","clientId":"D1230912380912830","clientIdType":"DID","payeeAccountNo":"X1000101010100101","payeeName":"被付款方名"},"payerInfo":{"certificateNumber":"大壮","clientId":"D12038901283102938","clientIdType":"DID","payerName":"付款方名","reservedPhone":"15015011501"},"sourceCurrency":"USD","subject":"D12038901283102938","subjectType":"DID","targetCurrency":"CNY"},"sourceApp":"FMPayout","subject":"D12038901283102938","subjectType":"DID"}

    }

    @RolesAllowed(RoleRegister.ROLE_COMMON_SERVICE)
    @Internal
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "service smb-fee@test", required = false, paramType = "header"),
            @ApiImplicitParam(name = "appId", value = "test@smb-fee", required = false, paramType = "header"),
    })
    public Bill billing(@RequestBody BillingRequest request) {
        RepeatDo repeatDo = RepeatDo.builder().repeatKey(request.identify()).scope(request.getClass().getName()).build();
        repeatDo.setCreatedBy("SYSTEM");
        repeatDo.setUpdatedBy("SYSTEM");
        repeatDo.setCreated(new Date(request.getBillingTime()));
        BillingContext context = new BillingContext();
        context.setRequest(Request.from(request));
        context.setTrial(false);
        CompletableFuture<BillingContext> future = new CompletableFuture<>();
        BillingContextDo contextDo = billingContextConvert.toDo(context);
        BillingRequestDo requestDo = BillingRequestConvert.toDo(request);
        try {
            Long id = txTemplate.execute(transactionStatus -> {
                repeatRepository.save(repeatDo);
                billingRequestRepository.save(requestDo);
                contextRepository.save(contextDo);
                return contextDo.getId();
            });
            context.setId(id);
        } catch (DuplicateKeyException e) {
            BillingContextDo retDo = contextRepository.findByRepeatKey(request.identify());
            context = billingContextConvert.toContext(retDo);
        }
        BillingStage stage = context.resume(future);
        springContext.publishEvent(stage);
        //处理同步返回
        try {
            context = future.get();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        Bill resp = context.getBill();
        return resp;
    }

    @Override
    public BillList batchTrial(BatchCmd request) {
        return null;
    }

    @Override
    public BillList batchBilling(BatchCmd request) {
        return null;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "service smb-fee@test", required = false, paramType = "header"),
            @ApiImplicitParam(name = "appId", value = "test@smb-fee", required = false, paramType = "header"),
    })
    @RolesAllowed(RoleRegister.ROLE_COMMON_SERVICE)
    @Internal
    public Bill trial(@RequestBody BillingRequest request) {
        BillingContext context = new BillingContext();
        context.setRequest(Request.from(request));
        context.setTrial(true);
        CompletableFuture<BillingContext> future = new CompletableFuture<>();
        BillingRequestReceived stage = (BillingRequestReceived) context.resume(future);
        springContext.publishEvent(stage);
        //处理同步返回
        try {
            context = future.get();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        Bill resp = context.getBill();
        return resp;
    }


}
