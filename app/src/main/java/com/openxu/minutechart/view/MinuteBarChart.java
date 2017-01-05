package com.openxu.minutechart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.openxu.minutechart.bean.MinuteParame;
import com.openxu.minutechart.bean.MinutesBean;
import com.openxu.minutechart.config.Constants;
import com.openxu.minutechart.utils.GlFontUtil;
import com.openxu.minutechart.utils.TouchEventUtil;

import java.util.List;

/**
 * author : openXu
 * create at : 2016/07/8 12:40
 * blog : http://blog.csdn.net/xmxkf
 * gitHub : https://github.com/openXu
 * project : StockChart
 * class name : MinuteBarChart
 * version : 1.0
 * class describe：分时底部状态图
 */
public class MinuteBarChart extends Chart {

    private List<MinutesBean> datas;
    private MinuteParame parame;            //通过计算得到的相关表格参数
    private String[] leftLable=new String[]{"0",""}; //最大成交手数（数量+单位数组）
    private int maxValue;                   //最大成交手数

    private RectF rectF = new RectF();      //图表矩形范围

    private float prePrice;                 //昨收价

    private float barW;                     //柱状宽度

    private boolean isDrawFinished = false;
    private boolean isEnable = true;

    public void setPrePrice(float prePrice){
        this.prePrice = prePrice;
    }
    public MinuteBarChart(Context context) {
        this(context, null);
    }


    public MinuteBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        touchEventUtil = new TouchEventUtil(context, this);
    }

    @Override
    public int getMoveIndex(PointF point) {
       int index = (int) ((point.x - Constants.chartStart) * datasize / rectF.width());
        index = Math.max(0, Math.min(index, datasize - 1));
        return index;
    }

    public void setOnFocusChangeListener(TouchEventUtil.OnFoucsChangedListener listener) {
        if (touchEventUtil == null) touchEventUtil = new TouchEventUtil(context, this);
        touchEventUtil.setFoucsChangedListener(listener);
    }

    /**数据设置*/
    public void setData(List<MinutesBean> list, MinuteParame parame ) {
        this.datas = list;
        this.parame = parame;
        datasize= this.datas == null ?0 : this.datas.size();
        initLineData();
        invalidate();
    }
    private void initLineData() {
        if(this.parame!=null) {
            maxValue = parame.maxVolume;
            leftLable = parame.leftVolume;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            //当使用wrap_content时，View使用默认高度（横屏和竖屏高度不同），而不是填充整个父布局
            int height = Math.min(Constants.defaultChartHeightL, specSize);
            setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                    height);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
 isDrawFinished = false;

        initFrameRect();
        drawGridLine(canvas);
        if(datasize ==0 ){
            isDrawFinished = true;
            return;
        }
        drawLabelY(canvas);
        drawBars(canvas);
        isDrawFinished = true;
    }

    /**1、初始化图表矩形*/
    public void initFrameRect() {
        this.rectF.set(Constants.chartStart, 0, getWidth(), getHeight());
        this.distanceX = rectF.width() / 4;
        //柱状宽度=表格宽度/柱状数量/2（一半作为空隙）
        barW = rectF.width() / Constants.MUNITE_NUMBER / 2;
    }
    /**2、绘制网格*/
    private void drawGridLine(Canvas canvas){
        float x, y;
        Path path = new Path();
        //绘制y轴方向格线
        for (int i = 0; i < 5; i++) {
            path.reset();
            x = Constants.chartStart + i * distanceX;
            switch (i){
                case 0:
                    gridlinePaint.setColor(Constants.C_AROUND_LINE);
                    canvas.drawLine(x+ Constants.gridlinewidth/2, 0,
                            x+ Constants.gridlinewidth/2, rectF.bottom, gridlinePaint);
                    break;
                case 4:
                    gridlinePaint.setColor(Constants.C_AROUND_LINE);
                    canvas.drawLine(x- Constants.gridlinewidth/2, 0,
                            x- Constants.gridlinewidth/2, rectF.bottom, gridlinePaint);
                    break;
                default:
                    path.moveTo(x, 0);
                    path.lineTo(x, rectF.bottom);
                    gridlinePaint.setColor(Constants.C_GRID_LINE);
                    canvas.drawPath(path, gridlinePaint);
                    break;
            }
        }
        //绘制x轴方向格线
        for(int j =0;j<2;j++){
            y = rectF.bottom * j;
            if(j==0)
                y=y+ Constants.gridlinewidth/2;
            else y = y- Constants.gridlinewidth/2;
            canvas.drawLine(Constants.chartStart, y, rectF.right, y, gridlinePaint);
        }
    }
    /**3、Y轴刻度：成交手*/
    private void drawLabelY(Canvas canvas) {
       /* if(orientation == Configuration.ORIENTATION_PORTRAIT)
            return;*/
        Constants.labelPaint.setColor(Constants.C_LABLE_TEXT);
        PointF pointF = getPointFY(Constants.labelPaint, 0);
        canvas.drawText("0", pointF.x, pointF.y, Constants.labelPaint);
        pointF = getPointFY(Constants.labelPaint, 1);
        canvas.drawText(leftLable[0]+leftLable[1], pointF.x, pointF.y, Constants.labelPaint);
    }

    /**4、绘制柱状*/
    private void drawBars(Canvas canvas) {
        Paint paint;
        float lastPrice= prePrice;    //prePrice昨收
        MinutesBean minutesBean;
        for (int i = 0; i < datasize; i++) {
            minutesBean = datas.get(i);
            float bl = minutesBean.cjnum*1.0f / maxValue;    //某一时刻成交量/最大成交量
            PointF p = getPoint(rectF, i, bl);
            //根据实时价格和前一个价格的大小，用不同颜色的柱状图
            if (minutesBean.cjprice < lastPrice) {    //如果成交价 小于 上一个价格（跌）
                paint = Constants.fallPaint;
            }else if(minutesBean.cjprice >lastPrice){ //如果成交价 大于 上一个价格（涨）
                paint = Constants.raisePaint;
            }else {
                paint = Constants.NomalPaint;
            }
            canvas.drawRect(p.x, p.y, p.x + barW, rectF.bottom, paint);
            lastPrice= minutesBean.cjprice;
        }
    }

    /**获取Y轴成交手数刻度的坐标*/
    private PointF getPointFY(Paint paint, int p) {
        float leading = GlFontUtil.getFontLeading(paint);   //baseLine
        float h = GlFontUtil.getFontHeight(paint);   //文字高度

        //X坐标：如果表格与左边没有留空间，就贴着表格写在里面；否则=控件-字宽度-间距
        if (p == 0) {
            float w = GlFontUtil.getFontlength(paint,"0");
            float x = Constants.chartStart == 0 ? 0 : rectF.left- w - Constants.S_LABLE_CHART_DIS;
            return new PointF(x, getHeight() - h + leading);
        } else if (p == 1) {
            float w = GlFontUtil.getFontlength(paint,leftLable[0]+leftLable[1]);
            float x = Constants.chartStart == 0 ? 0 : rectF.left- w - Constants.S_LABLE_CHART_DIS;
            return new PointF(x, leading);
        }
        return new PointF();
    }

    /**获取单个柱状坐标*/
    public PointF getPoint(RectF rect, int index, float scaleY) {
        PointF point = new PointF();
        //柱状图X坐标 = 图表开始坐标+表格宽度/分时数据数量242 * 索引
        point.x = rect.left + rect.width()/ Constants.MUNITE_NUMBER * (index * 1.0f);
        //Y坐标 = 图表上面的坐标 + 图表高度 * （1-价格比例） （注意Y轴坐标向下为正）
        point.y = rect.top + rect.height() * (1 - scaleY);
        return point;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isDrawFinished)return true;
        if(!isEnable || datasize ==0) return super.onTouchEvent(event);
        return touchEventUtil.onTouchEvent(event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }




}
