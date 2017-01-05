package com.openxu.minutechart.config;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.TypedValue;

import com.openxu.minutechart.utils.CommonUtil;
import com.openxu.minutechart.utils.GlFontUtil;

import java.text.DecimalFormat;

/**
 * author : openXu
 * create at : 2016/07/8 12:40
 * blog : http://blog.csdn.net/xmxkf
 * gitHub : https://github.com/openXu
 * project : StockChart
 * class name : Constants
 * version : 1.0
 * class describe：常量
 */
public class Constants {
    /**四个接口不予暴露*/
    //获取股票基本信息
    //获取分时图数据
    //获取K线图数据
    //获取盘口数据
    public static final String MinuteRequetUrl="http://blog.csdn.net/xmxkf";
    public static final String BasicDataRequestUrl="http://blog.csdn.net/xmxkf";
    public static final String KeylineRequestUrl="http://blog.csdn.net/xmxkf";
    public static final String PankouRequestUrl="http://blog.csdn.net/xmxkf";

    //分时开始时间（开盘时间9:30）
    public static final  long startTime = 9 * 60 * 60 * 1000 + 30 * 60 * 1000;
    //分时数据分隔时间,这里需要特殊处理
    public static final long startDivderTime = 13 * 60 * 60 * 1000;
    //分时图总时间长度4小时
    public static final long lenthTime = 4*60*60*1000;
    //分时数据数量
    public static final int MUNITE_NUMBER=242;  //60*4 一分钟一个，四小时

    //Handler消息
    public final static int MinutDataRefresh = 0x01;
    public final static int PankouDataRefresh = 0x02;
    public final static int FocusCancelFlag = 0x03;  //焦点结束
    public final static int FocusChangeFlag = 0x04;  //焦点位置变化

    //两位小数格式化
    public static final DecimalFormat twoPointFormat = new DecimalFormat("#0.00");
    public static final DecimalFormat noFormat = new DecimalFormat("0");


    public static int chartStart;               //图表左边开始的位置

    public static float pathlinewidth=0.5f;

    /**大小*/
    //默认的K线或者分时竖屏状态下的高度dip
    public static int defaultChartHeight = 100;
    //竖屏状态下成交量图和技术指标图的高度dip
    public static int defaultChartHeightL = 50;
    //网格线宽度dip
    public static float gridlinewidth=0.3f;

    public final static int S_LABLE_TEXT = 10;        // 普通标签字体大小
    public static int S_LABLE_CHART_DIS = 3;          // 字与表格的距离

    public static int M_TEXT_Y_MARGIN = 5;            //分时图Y轴刻度与上下边缘间距

    /*分时图MinuteHourChart*/
    public static float M_LINE_WIDTH = 0.7f;          //分时图价格曲线和均线 画笔宽度


    /**各种颜色配置*/
    public final static int C_LABLE_TEXT = Color.DKGRAY;        // 普通标签字体颜色
    public final static int C_RED_TEXT = 0xffff5353;            // 红色字体
    public final static int C_GREEN_TEXT = Color.parseColor("#209020"); // 绿色字体
    public final static int C_GRID_LINE = Color.parseColor("#DDDDDD");//网格线颜色
    public final static int C_AROUND_LINE = Color.parseColor("#EEEEEE"); //图表四周边界线颜色
    /*分时图MinuteHourChart中的颜色*/
    public static int C_M_AVG_LINE=0xffff5353;                  //分时图中均线画笔颜色
    public static int C_M_DATA_LINE=Color.parseColor("#6fc2f6");//分时图价格线画笔颜色
    public static int C_M_BG_1=Color.parseColor("#CCCCCC");//分时图线下背景色
    public static int C_M_BG_2=Color.parseColor("#FFFFFF");//分时图线下背景色

    public final static int C_RED_PILLAR = 0xffff5353;     // 红色柱
    public final static int C_GREEN_PILLAR = 0xff00b07c;   // 绿色柱

    //是否手指触摸时间结束
//    public static boolean isEnd = false;



    /**画笔*/
    public static Paint raisePaint,fallPaint,NomalPaint;   //涨、跌、正常状态下的柱状画笔
    public static Paint labelPaint;                        //字体绘制画笔


    public static void init(Context context){
        if(pathlinewidth!=0.5f)
            return;   //避免重复换算

        pathlinewidth = CommonUtil.dip2px(context,0.5f);

        defaultChartHeight= CommonUtil.dip2px(context,defaultChartHeight);
        defaultChartHeightL= CommonUtil.dip2px(context, defaultChartHeightL);
        gridlinewidth = CommonUtil.dip2px(context,gridlinewidth);
        M_TEXT_Y_MARGIN = CommonUtil.dip2px(context,M_TEXT_Y_MARGIN);
        S_LABLE_CHART_DIS = CommonUtil.dip2px(context, S_LABLE_CHART_DIS);
        M_LINE_WIDTH = CommonUtil.dip2px(context, M_LINE_WIDTH);


        labelPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                S_LABLE_TEXT, context.getResources().getDisplayMetrics()));
        chartStart = (int) GlFontUtil.getFontlength(labelPaint,"000.00一");

    }

    static{
        //涨
        raisePaint = new Paint();
        raisePaint.setAntiAlias(true);
        raisePaint.setColor(Constants.C_RED_PILLAR);
        //跌
        fallPaint = new Paint();
        fallPaint.setAntiAlias(true);
        fallPaint.setColor(Constants.C_GREEN_PILLAR);
        //正常
        NomalPaint = new Paint();
        NomalPaint.setAntiAlias(true);
        NomalPaint.setColor(Color.LTGRAY);

        labelPaint = new Paint();
        labelPaint.setAntiAlias(true);
        labelPaint.setColor(Constants.C_LABLE_TEXT);

    }

}
