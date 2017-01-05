package com.openxu.minutechart.bean;


import android.graphics.PointF;

import java.io.Serializable;

/**
 * author : openXu
 * create at : 2016/07/8 13:40
 * blog : http://blog.csdn.net/xmxkf
 * gitHub : https://github.com/openXu
 * project : StockChart
 * class name : MinutesBean
 * version : 1.0
 * class describe：分时数据实体类
 */
public class MinutesBean implements Serializable{
    public String time;                        //时间
    public float cjprice;                      //成交价
    public int cjnum=0;                        //成交量
    public long timesecond=0;                  //时间毫秒数，用于确定分时数据的x坐标
    public float avprice = Float.NaN;          //总的成交金额除以成交股数就是这个时间的平均价

//    public int color = 0xff000000;

    public PointF pointF=new PointF();         //该数据在分时图中的坐标（绘制的时候计算）

    @Override
    public String toString() {
        return "MinutesBean{" +
                "time='" + time + '\'' +
                ", cjprice=" + cjprice +
                ", cjnum=" + cjnum +
                ", timesecond=" + timesecond +
                ", avprice=" + avprice +
//                ", color=" + color +
                ", pointF=" + pointF +
                '}';
    }


}
