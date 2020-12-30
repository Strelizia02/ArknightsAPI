package com.strelizia.arknights.model;

/**
 * @author wangzy
 * @Date 2020/12/11 10:04
 * OPQ返回消息封装
 **/
public class SendMsgRespInfo {
    private String Msg;
    private Integer Ret;

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public Integer getRet() {
        return Ret;
    }

    public void setRet(Integer ret) {
        Ret = ret;
    }
}
