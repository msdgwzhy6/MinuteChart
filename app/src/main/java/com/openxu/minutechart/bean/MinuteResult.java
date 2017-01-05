package com.openxu.minutechart.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author : openXu
 * create at : 2016/07/8 13:40
 * blog : http://blog.csdn.net/xmxkf
 * gitHub : https://github.com/openXu
 * project : StockChart
 * class name : MinuteResult
 * version : 1.0
 * class describe：分时数据请求结果
 */
public class MinuteResult implements Serializable{
    private int code;
    private String msg;

    private List<MinutesBean> data=null;

    public List<MinutesBean> getData() {
        return data;
    }

    public void setData(List<MinutesBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MinuteResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
