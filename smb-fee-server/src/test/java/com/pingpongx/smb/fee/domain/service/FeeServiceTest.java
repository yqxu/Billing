package com.pingpongx.smb.fee.domain.service;

import com.alibaba.fastjson2.JSON;
import com.pingpongx.smb.fee.api.dtos.expr.ExprDto;
import com.pingpongx.smb.fee.dal.repository.BillingContextRepository;
import com.pingpongx.smb.fee.dal.repository.BillingRequestRepository;
import com.pingpongx.smb.fee.dal.repository.RepeatRepository;
import com.pingpongx.smb.fee.server.rpc.BillingServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.support.TransactionTemplate;

public class FeeServiceTest extends AbstractFeeTestDependency {

    @Mock
    RepeatRepository repeatRepository;
    @Mock
    BillingRequestRepository billingRequestRepository;
    @Mock
    BillingContextRepository contextRepository;
    @Mock
    TransactionTemplate txTemplate;
    @Mock
    ApplicationContext springContext;
    @InjectMocks
    BillingServiceImpl feeService;

    @Test
    public void testCreate() {
        JSON.toJSONString(generateBillingRequest());

        JSON.parseObject(JSON.toJSONString(generateFixDto()), ExprDto.class);
        JSON.parseObject(JSON.toJSONString(generateAXpBDto()), ExprDto.class);
        JSON.parseObject(JSON.toJSONString(generateTierDto()), ExprDto.class);

        JSON.parseObject(JSON.toJSONString(generateMaxDto()), ExprDto.class);
        JSON.parseObject(JSON.toJSONString(generateMinDto()), ExprDto.class);

        JSON.toJSONString(generateMatchRule());
        JSON.toJSONString(matchRule());

    }
}
