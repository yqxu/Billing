package com.pingpongx.smb.fee.domain.entity.express;

import com.pingpongx.smb.metadata.Identified;
import com.pingpongx.smb.fee.domain.runtime.BillingContext;
import com.pingpongx.smb.metadata.VariableDef;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Data
@Slf4j
public class AXpB implements BasicExpr,Calculator, Identified {
    BigDecimal a;
    BigDecimal b;

    VariableDef x;

    @Override
    public String identify() {
        StringBuilder builder = new StringBuilder();
        builder.append(a).append("*").append(x.getCode()).append("+").append(b);
        return builder.toString();
    }

    @Override
    public Calculator getCalculator() {
        return this;
    }


    @Override
    public BigDecimal getCalculateResult(BillingContext context) {
        BigDecimal ret = context.getCache().get(this.identify());
        if (ret !=null ){
            return ret;
        }
        BigDecimal xVal = new BigDecimal(x.extractor().doExtract(x,context).toString());
        log.info("variable x is "+xVal);
        ret = xVal.multiply(a).add(b);
        log.info("result : "+ret);
        context.getCache().put(this.identify(),ret);
        return ret;
    }

}
