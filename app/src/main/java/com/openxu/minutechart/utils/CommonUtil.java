package com.openxu.minutechart.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.openxu.minutechart.config.Constants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.UUID;

/**
 * author : openXu
 * create at : 2016/07/8 12:40
 * blog : http://blog.csdn.net/xmxkf
 * gitHub : https://github.com/openXu
 * project : StockChart
 * class name : CommonUtil
 * version : 1.0
 * class describe：
 */
public class CommonUtil {

    /**
     * 获取设备id；
     * 获取用户设备的IMEI，通过IMEI和mac来唯一的标识用户。
     * ACCESS_WIFI_STATE(必须)	获取用户设备的mac地址，
     * <p/>
     * 在平板设备上，无法通过IMEI标示设备，我们会将mac地址作为用户的唯一标识
     * 在模拟器中运行时，IMEI返回总是000000000000000。
     *
     * @param context
     * @return
     */
    public static String getUUID(Context context) {
        if (context == null) {
            return "";
        }
        final TelephonyManager tm = (TelephonyManager) context.
                getSystemService(Context.TELEPHONY_SERVICE);
        String tmDeviceId = "";
        String androidId = "";
        String mac = "";

        String serial = Build.SERIAL; //12位；
        String time = Build.TIME + "";//13位；

        if (tm != null) {
            tmDeviceId = "" + tm.getDeviceId();
        }

        androidId = "" + Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
//        mac = "" + getMacAdress(context);

        Log.e("@@@","tmDeviceId=" + tmDeviceId + ",\nandroidId=" + androidId
                + ",\nmac=" + mac);

        long uuidParam2;

        uuidParam2 = (long) (androidId.hashCode() << 32 | (serial + time).hashCode());

        UUID deviceUuid = new UUID(tmDeviceId.hashCode(), uuidParam2);
        //生成32位的识别码；
        String uniqueId = deviceUuid.toString();
        String encryption = encryption(uniqueId);
        Log.e("@@@","encryption====>" + encryption);
        return encryption;
    }




    public static String encryption(String plainText) {
        String re_md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i = 0;

            StringBuilder sb = new StringBuilder("");
            for (int offset = 0, len = b.length; offset < len; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(i));
            }
            re_md5 = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }





    public static boolean isEmpty(String text) {
        if (TextUtils.isEmpty(text)) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String text) {
        if (!TextUtils.isEmpty(text)) {
            return true;
        }
        return false;
    }



    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        return (int) (context.getResources().getDisplayMetrics().widthPixels + 0.5);
    }

    public static int getScreenHeight(Context context) {
        return (int) (context.getResources().getDisplayMetrics().heightPixels + 0.5);
    }







    /**
     * 测试网络连接问题；
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        if (context != null) {
            ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conn != null) {
                NetworkInfo info = conn.getActiveNetworkInfo();
                return (info != null && info.isConnected());
            }
        }
        return false;
    }

    public static String[] getDisplayVolume(int dot, float volume) {
        String unit = "";
        DecimalFormat df = Constants.twoPointFormat;
        if (dot >= 11) {
//            volume /= 1E10;
//            unit = "百亿";
            volume /= 1E8;
            unit = "亿";
            df=null;
        } else
        if (dot >= 9) {
            volume /= 1E8;
            unit = "亿";
        }
        else if (dot >= 7) {
//            volume /= 1E6;
//            unit = "百万";
            volume /= 1E4;
            unit = "万";
            df = null;
        }
        else if (dot >= 5) {
            volume /= 1E4;
            unit = "万";
        } else {
            df = Constants.noFormat;
        }
        return new String[]{getNumber(volume, df), unit};

    }
    public static String getNumber(float number, NumberFormat df) {
        number = getFloatFromString(Float.toString(number));
        if(df==null){
            int index = String.valueOf(Math.ceil(number)).indexOf(".");
            if(index ==-1)return String.valueOf(Math.ceil(number));
            return String.valueOf(Math.ceil(number)).substring(0,index);
        }
        return df.format(number);
    }

    private static float getFloatFromString(String s) {
        if(s != null && !s.trim().equals("")){
            s = s.replaceAll(",", "");

            try {
                if(s.endsWith("%")){
                    return Float.valueOf(s.substring(0, s.length()-1));
                }
                else{
                    return Float.valueOf(s);
                }
            } catch(NumberFormatException e){
            }
        }

        return 0;
    }




}
