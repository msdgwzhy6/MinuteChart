package com.openxu.minutechart.bean;

/**
 * author : openXu
 * create at : 2016/07/8 13:40
 * blog : http://blog.csdn.net/xmxkf
 * gitHub : https://github.com/openXu
 * project : StockChart
 * class name : StockBaseResult
 * version : 1.0
 * class describe：接口返回的股票基本信息
 */
public class StockBaseResult {

    private StockBaseInfo data=null;

    public StockBaseInfo getData() {
        return data;
    }

    public void setData(StockBaseInfo data) {
        this.data = data;
    }



}
