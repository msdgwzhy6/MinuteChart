package com.openxu.minutechart.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PointF;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.openxu.minutechart.config.Constants;
import com.openxu.minutechart.view.Chart;


/**
 * author : openXu
 * create at : 2016/07/8 12:40
 * blog : http://blog.csdn.net/xmxkf
 * gitHub : https://github.com/openXu
 * project : StockChart
 * class name : TouchEventUtil
 * version : 1.0
 * class describe： 行情图的手指触摸事件处理类
 */
public class TouchEventUtil {

    private String TAG = "TouchEventUtil";

    private Context mcontext;
    private Chart chartView;

    //移动的阈值
    private int mTouchSlop;

    private HandlerThread handlerThread = null;
    private Handler handler = null;
    private OnFoucsChangedListener foucsChangedListener;
    //焦点坐标
    private PointF focusPoint = new PointF();

    public interface OnFoucsChangedListener {
        void foucsChanged(int tag, int index);
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public TouchEventUtil(Context context, Chart view) {
        this.mcontext = context;
        this.chartView = view;
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        initHandler();
    }

    public void setFoucsChangedListener(OnFoucsChangedListener foucsChangedListener) {
        this.foucsChangedListener = foucsChangedListener;
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private void initHandler() {
        handlerThread = new HandlerThread("focus");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper(),new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case Constants.FocusCancelFlag:
                    case Constants.FocusChangeFlag:
                        if (foucsChangedListener != null)
                            foucsChangedListener.foucsChanged(msg.what, (Integer) msg.obj);
                        break;
                }
                return true;
            }
        });

    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
//                Constants.isEnd = false;
                focusPoint.set(event.getX(), event.getY());
                touchFocusMove(focusPoint, false);
                break;
            case MotionEvent.ACTION_MOVE:
                focusPoint.set(event.getX(), event.getY());
                touchFocusMove(focusPoint, false);
                break;
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                touchFocusMove(null, true);
                break;
        }
        return true;
    }

    /**焦点滑动*/
    public void touchFocusMove(final PointF point, final boolean outable) {
        if(chartView == null||chartView.datasize==0)
            return;
        Message msg;
        if (outable) {
            //焦点结束
            msg = handler.obtainMessage(Constants.FocusCancelFlag);
            msg.obj = 0;
            msg.sendToTarget();
        } else if (point != null) {
            //焦点位置变化
            int index = chartView.getMoveIndex(point);
            msg = handler.obtainMessage(Constants.FocusChangeFlag);
            msg.obj = index;
            handler.sendMessage(msg);
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public void destroy() {
        if (handler != null) handler.removeCallbacksAndMessages(null);
        if (handlerThread != null) {
            handlerThread.quit();
            handlerThread = null;
        }
    }


}
