package com.pingpongx.smb.metadata;

public abstract class AbstractVariable implements VariableDef {
    String path;
    String code;
    boolean isNum;
    String namespace;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isNum() {
        return isNum;
    }

    public void setIsNum(boolean isNum) {
        this.isNum = isNum;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
