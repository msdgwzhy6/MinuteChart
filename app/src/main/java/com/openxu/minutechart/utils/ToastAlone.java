package com.openxu.minutechart.utils;

import android.app.Application;
import android.widget.Toast;


/**
 * author : openXu
 * create at : 2016/11/8 14:05
 * blog : http://blog.csdn.net/xmxkf
 * gitHub : https://github.com/openXu
 * project : StockChart
 * class name : ToastAlone
 * version : 1.0
 * class describe：
 */
public class ToastAlone{
    private static Application mApp;
    /**
     * 唯一的toast
     */
    private static Toast mToast = null;

    public static void init(Application context) {
    	mApp = context;
    }
     

    public static Toast showToast(int stringid, int lastTime) {
        if (mToast != null) {
            //mToast.cancel();
        } else {
            mToast = Toast.makeText(mApp, stringid, lastTime);
        }
        mToast.setText(stringid);
        mToast.show();
        return mToast;
    }

    public static Toast showToast(String tips, int lastTime) {
        if (mToast != null) {
            //mToast.cancel();
        } else {
            mToast = Toast.makeText(mApp, tips, lastTime);
        }
        mToast.setText(tips);
        mToast.show();
        return mToast;
    }

    public static void show(String text){
        if(null == mToast){
            mToast = Toast.makeText(mApp, text, Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.show();
    }
    public static void show(int textRid){
        if(null == mToast){
            mToast = Toast.makeText(mApp, textRid, Toast.LENGTH_SHORT);
        }
        mToast.setText(textRid);
        mToast.show();
    }
}