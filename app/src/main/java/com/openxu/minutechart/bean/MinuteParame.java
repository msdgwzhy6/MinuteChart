package com.openxu.minutechart.bean;

import java.io.Serializable;

/**
 * author : openXu
 * create at : 2016/07/8 13:40
 * blog : http://blog.csdn.net/xmxkf
 * gitHub : https://github.com/openXu
 * project : StockChart
 * class name : ChartParame
 * version : 1.0
 * class describe：图标需要的各种参数，方便类之间的传递
 */
public class MinuteParame implements Serializable{

    public int pricedivider;               //分时数据2个小时数据个数

    /**分时图使用*/
    public float minPrice=Float.MAX_VALUE; //最低价
    public float maxPrice=0;               //最高价
    public float maxpriceLenth;            //最高价字符长度
    public float volumeper;                //最大跌涨幅

    /**分时成交量图使用*/
    public int maxVolume;                  //最大成交量
    public String[] leftVolume;            //最大成交量（数量+单位数组）

}
