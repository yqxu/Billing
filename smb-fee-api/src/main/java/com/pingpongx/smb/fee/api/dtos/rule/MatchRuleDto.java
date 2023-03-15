package com.pingpongx.smb.fee.api.dtos.rule;

import com.alibaba.fastjson2.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pingpongx.smb.export.module.persistance.RuleDto;
import com.pingpongx.smb.fee.api.dtos.expr.*;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({@JsonSubTypes.Type(value = FixDto.class, name = "Fix"), @JsonSubTypes.Type(value = AXpBDto.class, name = "AXpB"), @JsonSubTypes.Type(value = MaxDto.class, name = "Max"), @JsonSubTypes.Type(value = MinDto.class, name = "Min"), @JsonSubTypes.Type(value = TierDto.class, name = "Tier")})
@JSONType(seeAlso = {FixDto.class, AXpBDto.class, MaxDto.class, MinDto.class, TierDto.class})
public abstract class MatchRuleDto implements Serializable {
    public abstract String getType();
    public abstract RuleDto toEngineRule();
}