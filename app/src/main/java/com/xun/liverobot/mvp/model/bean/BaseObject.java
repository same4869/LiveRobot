package com.xun.liverobot.mvp.model.bean;

import java.io.Serializable;

/**
 * bean基类，所有bean都继承它
 */

public class BaseObject implements Serializable {
    private int code;
    private String error;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isSucceed() {
        return code == 0;
    }
}
