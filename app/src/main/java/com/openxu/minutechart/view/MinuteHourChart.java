package com.openxu.minutechart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Shader;
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
 * class name : MinuteHourChart
 * version : 1.0
 * class describe：分时图表控件
 */
public class MinuteHourChart extends Chart {

    private List<MinutesBean> datas;   //分时图数据
    private MinuteParame parame;       //通过计算得到的相关表格参数

    private Paint paintbg;             //轮廓背景画笔
    private Paint minuteAvgPaint;      //分时图中均线画笔
    private Paint pathPaint;           //价格线画笔

    private boolean isDrawFinished = false;
    private boolean isEnable = true;        //是否可触摸


    public MinuteHourChart(Context context) {
        this(context, null);
        this.context = context;
    }

    public MinuteHourChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        touchEventUtil = new TouchEventUtil(context, this);
        init();
    }

    private void init() {
        initPaint();
    }

    private void initPaint() {
        /*价格线下背景画笔*/
        paintbg = new Paint();
        paintbg.setAlpha(50);
        LinearGradient linearGradient = new LinearGradient(Constants.chartStart, 0,
                Constants.chartStart, lineheight, Constants.C_M_BG_1, Constants.C_M_BG_2,
                Shader.TileMode.MIRROR);
        paintbg.setShader(linearGradient);

        /*价格线画笔*/
        pathPaint = new Paint();
        pathPaint.setAntiAlias(true);
        pathPaint.setColor(Constants.C_M_DATA_LINE);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeWidth(Constants.M_LINE_WIDTH);

        /*均线画笔*/
        minuteAvgPaint=new Paint();
        minuteAvgPaint.setStyle(Paint.Style.STROKE);
        minuteAvgPaint.setAntiAlias(true);
        minuteAvgPaint.setStrokeWidth(1f);
        minuteAvgPaint.setColor(Constants.C_M_AVG_LINE);
        minuteAvgPaint.setStrokeJoin(Paint.Join.ROUND);
        minuteAvgPaint.setStrokeWidth(Constants.M_LINE_WIDTH);
    }


    public void setData(List<MinutesBean> list, MinuteParame parame) {
        this.datas = list;
        this.parame = parame;
        this.datasize = datas == null ? 0 : datas.size();
        //重绘
        invalidate();
    }


    /**焦点事件*/
    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isDrawFinished)
            return true;
        if (!isEnable || datasize == 0)
            return super.onTouchEvent(event);
        return touchEventUtil.onTouchEvent(event);
    }

    @Override
    public int getMoveIndex(PointF pointF) {
        int index = (int) ((pointF.x - Constants.chartStart) * Constants.MUNITE_NUMBER / linewidth);
        index = Math.max(0, Math.min(index, datasize - 1));
        return index;
    }

    public void setOnFocusChangeListener(TouchEventUtil.OnFoucsChangedListener listener) {
        if (touchEventUtil == null)
            touchEventUtil = new TouchEventUtil(context, this);
        touchEventUtil.setFoucsChangedListener(listener);
    }


    /**
     * 测量分时图的大小：
     * onMeasure()方法详解请参考：
     *              http://blog.csdn.net/xmxkf/article/details/51490283
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //图表宽度配置为wrap_content的时候处理为填充父窗体，宽度的测量不需要重写
        //图表高度配置为wrap_content的时候给定一个默认高度
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        int height;
        if (specMode == MeasureSpec.EXACTLY) {
            //match_content、xxdip、weight的情况，直接使用父控件建议的高度
            height = specSize;
        } else{
            //当使用wrap_content时，分时图使用默认高度和剩余高度的最小值，而不是填充整个父布局。
            height = Math.min(Constants.defaultChartHeight, specSize);
        }
        //设置控件宽高，宽度由View默认处理，高度为重新计算的高度
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        isDrawFinished = false;
        computVars();
        drawGridLine(canvas);    //绘制图表网格
        drawLabelX(canvas);      //绘制X轴时间刻度
        if (datas == null || datas.size() == 0) {
            isDrawFinished = true;
            return;
        }
        drawLabelY(canvas);       //绘制Y轴左边刻度（最高价、最低价）
        drawRightLabelY(canvas);  //绘制Y轴右边刻度
        drawDataPath(canvas);     //绘制价格曲线
        drawAvgLine(canvas);      //绘制平均线
        isDrawFinished = true;
    }

    private void computVars() {
        //Y轴方向网格线之间距离 = 总高度 - 时刻表高度 - 字体间距*2
        distanceY = (getHeight() - GlFontUtil.getFontHeight(Constants.labelPaint) -
                (Constants.S_LABLE_CHART_DIS*2)) / 4;
        linewidth = getWidth() - Constants.chartStart;
        distanceX = linewidth / 4;
        //图表的高度 = 总高度 - 时间刻度高度 - 字体间距
        lineheight = getHeight() - GlFontUtil.getFontHeight(Constants.labelPaint)
                - Constants.S_LABLE_CHART_DIS*2;
    }

    /**1、绘制图表格子线*/
    private void drawGridLine(Canvas canvas) {
        float x, y;
        Path path = new Path();
        //绘制y轴方向格线
        for (int i = 0; i < 5; i++) {
            path.reset();
            x = Constants.chartStart + i * distanceX;
            switch (i){
                //两边的网格线颜色稍微亮一点
                case 0:   //分时图左边网格线
                    gridlinePaint.setColor(Constants.C_AROUND_LINE);
                    canvas.drawLine(x, 0, x, lineheight, gridlinePaint);
                    break;
                case 4:   //分时图右边网格线
                    gridlinePaint.setColor(Constants.C_AROUND_LINE);
                    canvas.drawLine(x- Constants.gridlinewidth/2, 0,
                            x- Constants.gridlinewidth/2, lineheight, gridlinePaint);
                    break;
                default:
                    gridlinePaint.setColor(Constants.C_GRID_LINE);
                    canvas.drawLine(x,0,x,lineheight,gridlinePaint);
                    break;
            }
        }
        //绘制x轴方向格线
        for (int j = 0; j < 5; j++) {
            y = distanceY * j;
            path.reset();
            switch (j){
                case 0:
                    gridlinePaint.setColor(Constants.C_AROUND_LINE);
                    canvas.drawLine(Constants.chartStart, y+ Constants.gridlinewidth/2, linewidth + Constants.chartStart, y+ Constants.gridlinewidth/2, gridlinePaint);
                    break;
                case 4:
                    gridlinePaint.setColor(Constants.C_AROUND_LINE);
                    canvas.drawLine(Constants.chartStart, y, linewidth + Constants.chartStart, y, gridlinePaint);
                    break;
                case 2:
                    DashPaint.setStrokeWidth(2f);
                    path.moveTo(Constants.chartStart, y);
                    path.lineTo(linewidth + Constants.chartStart, y);
                    canvas.drawPath(path, DashPaint);
                    break;
                default:
                    gridlinePaint.setColor(Constants.C_GRID_LINE);
                    canvas.drawLine(Constants.chartStart, y, linewidth + Constants.chartStart, y, gridlinePaint);
                    break;
            }
        }
    }

    /**2、绘制X轴时间刻度*/
    private void drawLabelX(Canvas canvas) {
        Constants.labelPaint.setColor(Constants.C_LABLE_TEXT);
        String[] labels = new String[]{"9:30", "10:30", "11:30/13:00", "14:00", "15:00"};
        for (int i = 0; i < 5; i++) {
            PointF pointF = getXPointF(Constants.labelPaint, labels[i], i);
            canvas.drawText(labels[i], pointF.x, pointF.y, Constants.labelPaint);
        }
    }
    /**3、绘制Y轴左刻度*/
    private void drawLabelY(Canvas canvas) {
        PointF pointF;
        for (int i = 0; i < 3; i++) {
            switch (i) {
                case 0:
                    //最低价，绿色字
                    Constants.labelPaint.setColor(Constants.C_GREEN_TEXT);
                    pointF = getYPointF(Constants.labelPaint, 0);
                    canvas.drawText(Constants.twoPointFormat.format(parame.minPrice),
                            pointF.x, pointF.y, Constants.labelPaint);
                    break;
                case 1:
                    break;
                case 2:
                    //最高价，红色
                    Constants.labelPaint.setColor(Constants.C_RED_TEXT);
                    pointF = getYPointF(Constants.labelPaint, 2);
                    canvas.drawText(Constants.twoPointFormat.format(parame.maxPrice),
                            pointF.x, pointF.y, Constants.labelPaint);
                    break;
            }

        }

    }
    /**4、绘制Y轴右刻度（跌涨幅）*/
    private void drawRightLabelY(Canvas canvas) {
        PointF pointF;
        String text;
        for (int i = 0; i < 3; i++) {
            switch (i) {
                case 0:
                    text = "-"+ Constants.twoPointFormat.format(parame.volumeper * 100)+"%";
                    Constants.labelPaint.setColor(Constants.C_GREEN_TEXT);
                    pointF = getRightPointF(Constants.labelPaint, 0, text);
                    canvas.drawText(text, pointF.x, pointF.y, Constants.labelPaint);
                    break;
                case 1:
                    break;
                case 2:
                    text = Constants.twoPointFormat.format(parame.volumeper * 100)+"%";
                    Constants.labelPaint.setColor(Constants.C_RED_TEXT);
                    pointF = getRightPointF(Constants.labelPaint, 2, text);
                    canvas.drawText(text, pointF.x, pointF.y, Constants.labelPaint);
                    break;
            }
        }
    }

    /**5、绘制数据曲线&渐变背景*/
    private void drawDataPath(Canvas canvas) {
        Path path = new Path();
        Path pathbg = new Path();

        //第一个数据
        PointF point = getDataPointF(0, false);
        path.moveTo(point.x, point.y);
        pathbg.moveTo(Constants.chartStart, lineheight);     //从图表左下角开始
        pathbg.lineTo(Constants.chartStart, point.y);
        pathbg.lineTo(point.x, point.y);
//        pathbg.moveTo(point.x, lineheight);

        PointF nextPoint;

        for (int i = 1; i < datasize - 1; i++) {
            nextPoint = getDataPointF(i, false);
            //quadTo：二阶贝塞尔曲线连接前后两点，这样使得曲线更加平滑
            path.quadTo(point.x, point.y, nextPoint.x, nextPoint.y);
            //背景沿着价格曲线
            pathbg.lineTo(nextPoint.x, nextPoint.y);
            point = nextPoint;
        }
        //最后一个数据
        nextPoint = getDataPointF(datasize - 1, false);
        path.quadTo(point.x, point.y, nextPoint.x, nextPoint.y);
        pathbg.lineTo(nextPoint.x, nextPoint.y);
        //背景轮廓回到表格右下角
        pathbg.lineTo(nextPoint.x, lineheight);
        //关闭当前轮廓。如果当前点不等于轮廓的第一点，则自动添加一个线段。
        pathbg.close();
        canvas.drawPath(path, pathPaint);
        canvas.drawPath(pathbg, paintbg);
    }

    /**6、绘制平均红色线*/
    private void drawAvgLine(Canvas canvas) {
        boolean flag=false;
        Path path = new Path();
        PointF point;
        for (int i = 0; i < datasize; i++) {
            if(datas.get(i).avprice==Float.MIN_VALUE)
                continue;
            if(!flag){
                point = getDataPointF(i, true);
                path.moveTo(point.x, point.y);
                flag=true;
            }else {
                point = getDataPointF(i, true);
                path.lineTo(point.x, point.y);
            }
        }

        canvas.drawPath(path, minuteAvgPaint);
    }

    /**获取时间刻度字体的位置*/
    private PointF getXPointF(Paint paint, String datestr, int p) {
        float w = GlFontUtil.getFontlength(paint, datestr);
        float h = GlFontUtil.getFontLeading(paint);
        //文字绘制baseLine为表格高度+间距+基准
        h = lineheight + Constants.S_LABLE_CHART_DIS + h;
        if (p == 0) {
            return new PointF(Constants.chartStart, h);
        } else if (p == 4) {
            return new PointF(Constants.chartStart + linewidth - w, h);
        }
        return new PointF(Constants.chartStart + p * distanceX - w / 2, h);
    }
    /**获取Y轴左刻度（价格）字体的位置*/
    private PointF getYPointF(Paint paint, int p) {
        float leading = GlFontUtil.getFontLeading(paint);  //baseLine
        float h = GlFontUtil.getFontHeight(paint);  //文字高度
        //X坐标：如果表格与左边没有留空间，就贴着表格写在里面；否则=控件-字宽度-间距
        float x = Constants.chartStart == 0 ? 0 : Constants.chartStart - getMaxPriceLenth() - Constants.S_LABLE_CHART_DIS;
        if (p == 0) {
            //最低价
            return new PointF(x, lineheight- Constants.M_TEXT_Y_MARGIN - h + leading);
        } else if (p == 2) {
            //最高价
            return new PointF(x, Constants.M_TEXT_Y_MARGIN + leading);
        }
        return new PointF();
    }
    /**获取Y轴右刻度字体的位置*/
    private PointF getRightPointF(Paint paint, int p, String str) {
        float leading = GlFontUtil.getFontLeading(paint);
        float h = GlFontUtil.getFontHeight(paint);
        float w = GlFontUtil.getFontlength(paint, str);
        float x = Constants.chartStart + linewidth - w;
        if (p == 0)
            return new PointF(x, lineheight - Constants.M_TEXT_Y_MARGIN- h + leading);
        else if (p == 2)
            return new PointF(x, leading+ Constants.M_TEXT_Y_MARGIN);

        return new PointF();
    }

    /**获取数据在屏幕上的像素点*/
    private PointF getDataPointF(int index, boolean isavg) {
        MinutesBean minutesBean = datas.get(index);
        float p = minutesBean.cjprice;
        //如果是均线，价格等于平均价
        if (isavg)
            p = minutesBean.avprice;

        long time = minutesBean.timesecond;

        PointF point = new PointF();
        //根据最高价和最低价，计算当前数据在图表上Y轴的坐标
        point.y = Constants.M_TEXT_Y_MARGIN +
                (lineheight - Constants.M_TEXT_Y_MARGIN * 2) *
                        (parame.maxPrice - p) / (parame.maxPrice - parame.minPrice);

        if (index >= parame.pricedivider) {
            float t = time - Constants.startDivderTime;
            point.x = Constants.chartStart + linewidth / 2 + t / (Constants.lenthTime) * linewidth;
            if (t < 0) {//预防服务器返回的数据不常规
                t = time - Constants.startTime;
                point.x = Constants.chartStart + t / (Constants.lenthTime) * linewidth;
            }
        } else {
            float t = time - Constants.startTime;   //与9:30的差值
            //x轴坐标：开始距离+差值/总时间*图表宽度
            point.x = Constants.chartStart + t / (Constants.lenthTime) * linewidth;
        }
        if (!isavg)
            minutesBean.pointF = point;

        return point;
    }

    public float getLinewidth() {
        return linewidth;
    }

    public float getMaxPriceLenth() {
        return parame.maxpriceLenth;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}

