package com.pingpongx.smb.fee.domain.entity;

import com.pingpongx.smb.fee.domain.enums.BizLine;

public class Subject {
    String code;
    String displayName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
