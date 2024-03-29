package com.pingpongx.smb.fee.domain.module.express;

import com.pingpongx.smb.fee.api.dtos.expr.ExprDto;
import com.pingpongx.smb.fee.api.dtos.expr.FixDto;
import com.pingpongx.smb.fee.domain.runtime.BillingContext;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class Fixed implements BasicExpr,Calculator{
    BigDecimal fix;

    public static Fixed of(BigDecimal fix){
        Fixed ret = new Fixed();
        ret.setFix(fix);
        return ret;
    }
    @Override
    public String identify() {
        StringBuilder builder = new StringBuilder();
        builder.append(fix);
        return builder.toString();
    }
    @Override
    public Calculator fetchCalculator() {
        return  this;
    }

    @Override
    public String getType() {
        return "Fix";
    }

    @Override
    public ExprDto toDto() {
        FixDto dto = new FixDto();
        dto.setFix(fix.toString());
        return dto;
    }

    @Override
    public BigDecimal getCalculateResult(BillingContext context) {
        return fix;
    }
}
