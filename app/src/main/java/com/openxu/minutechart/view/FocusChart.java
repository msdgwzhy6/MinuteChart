package com.openxu.minutechart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import com.openxu.minutechart.bean.MinutesBean;
import com.openxu.minutechart.utils.CommonUtil;

/**
 * author : openXu
 * create at : 2016/07/8 12:40
 * blog : http://blog.csdn.net/xmxkf
 * gitHub : https://github.com/openXu
 * project : StockChart
 * class name : FocusChart
 * version : 1.0
 * class describe：焦点图
 */
public class FocusChart extends View {

    private PointF focusP;
    private Paint linePaint;
    private Context context;
    private int width;
    private int start;
    private int height;
    private boolean frameinit = false;
    private boolean isCanceled = false;
    private boolean isLayout = false;

    public FocusChart(Context context) {
        this(context, null);
    }

    public FocusChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initPaint();
    }

    private void initPaint() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(CommonUtil.dip2px(context, 0.4f));
        linePaint.setColor(Color.YELLOW);
    }


    public void update(MinutesBean bean) {
        this.focusP = bean.pointF;

        if (isLayout) {
            requestLayout();
//            measure(width+start, height);
//            layout(0, 0, width, height);
//            invalidate();
        }else
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width == 0 ? widthMeasureSpec : (width+start), height == 0 ? heightMeasureSpec : height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isCanceled) return;
        canvas.drawColor(Color.TRANSPARENT);
        if (focusP == null)
            return;
        //水平参考线
        canvas.drawLine(start, focusP.y, start + width, focusP.y, linePaint);
        //数值参考线
        canvas.drawLine(focusP.x, 0, focusP.x, height, linePaint);
        //中心点
        canvas.drawCircle(focusP.x, focusP.y, 3.0f, linePaint);
    }

    public void setStart(int start) {
        this.start = start;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public void setFrame(int width, int height,boolean isLayout) {
        this.width = width;
        this.height = height;
        this.isLayout = isLayout;
        frameinit = true;
    }

    public void setFrameinit(boolean isframeinit){
        this.frameinit = isframeinit;
    }

    public boolean getFrameInit() {
        return frameinit;
    }

    public void setIsLayout(boolean isLayout){
        this.isLayout = isLayout;
    }



}
