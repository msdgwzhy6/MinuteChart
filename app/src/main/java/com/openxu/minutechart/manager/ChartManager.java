package com.openxu.minutechart.manager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.openxu.minutechart.R;
import com.openxu.minutechart.bean.MinuteParame;
import com.openxu.minutechart.bean.MinutesBean;
import com.openxu.minutechart.bean.PankouData;
import com.openxu.minutechart.bean.StockBaseInfo;
import com.openxu.minutechart.bean.StockBaseResult;
import com.openxu.minutechart.config.Constants;
import com.openxu.minutechart.request.MinuteRequest;
import com.openxu.minutechart.testdata.TestData;
import com.openxu.minutechart.utils.CommonUtil;
import com.openxu.minutechart.utils.JSONUtil;
import com.openxu.minutechart.utils.TouchEventUtil;
import com.openxu.minutechart.view.FocusChart;
import com.openxu.minutechart.view.MinuteBarChart;
import com.openxu.minutechart.view.MinuteHourChart;

import java.util.List;


/**
 * author : openXu
 * create at : 2016/07/8 12:40
 * blog : http://blog.csdn.net/xmxkf
 * gitHub : https://github.com/openXu
 * project : StockChart
 * class name : StockChartManager
 * version : 1.0
 * class describe：股票图表库操作入口类
 */
public class ChartManager{
    private String TAG = "ChartManager";

    private String symbol;               //股票代码
    /**展示的activity以及其activity布局*/
    private Activity activity;
    protected View rootView;

    /**顶部lable控件，展示股票基本信息*/
    private View layout_baseinfo;      //顶部基本信息
    private TextView tv_name, tv_code, tv_stop,
            tv_price, tv_price_add, tv_price_addp,
            tv_open, tv_heigh,
            tv_vol, tv_low, tv_turnover, tv_amplitude;
    private LinearLayout ll_price;

    //加载框
    private View layout_loading;

    private StockBaseInfo gpInfo;       //股票基本信息
    private List<MinutesBean> datas;    //分时图数据
    private MinuteParame parame;
    private PankouData.Data panKouDatas;//盘口数据

    private View loadingView;
    private TextView tv_junxian, tv_shijian, tv_fenshi;
    private LinearLayout ll_lable;

    protected MinuteHourChart minuteHourView;   //分时图控件
    protected MinuteBarChart barView;           //底部状态图控件
    protected FocusChart focusView;             //焦点图
    protected LinearLayout pankoulinear;        //盘口数据容器
    private View pankouline;


    private MinuteRequest mRequestUtil;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.FocusCancelFlag:    //焦点结束
                    updateFocusView(true, datas.get(datas.size() - 1));
                    updateBaseInfoLable(datas.get(datas.size() - 1), true);
                    break;
                case Constants.FocusChangeFlag:    //焦点位置变化
                    MinutesBean minutesBean = (MinutesBean) msg.obj;
                    updateFocusView(false, minutesBean);
                    updateBaseInfoLable(minutesBean, false);
                    break;
                case Constants.MinutDataRefresh:
                    setLoadViewVisibilty(View.GONE);
                    //重绘分时图
                    minuteHourView.setData(datas,parame);
                    //重绘状态图
                    barView.setData(datas,parame);
                    updateBaseInfoLable(datas.get(datas.size() - 1), true);
                    updateTopLable(datas.get(datas.size()-1));
                    break;
                case Constants.PankouDataRefresh:
                    updatePankouData(panKouDatas);
                    updateTopLableByPankou(panKouDatas);
                    break;


            }
        }
    };

    /**
     * 管理器构造方法
     * @param activity 图表展示的activity
     * @param symbol 股票代码
     */
    public ChartManager(final Activity activity, String symbol) {
        this.activity = activity;
        rootView = ((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0);
        this.symbol = symbol;

        initView();
    }

    /**展示入口*/
    public void show() {
        setLoadViewVisibilty(View.VISIBLE);
        //TODO 模拟请求股票基本数据
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    String response= TestData.StockBaseData;
                    gpInfo = JSONUtil.jsonToBean(response,StockBaseResult.class).getData();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //请求成功后展示股票基本信息
                            showBaseInfo(gpInfo);
                            //开启分时图请求
                            if(mRequestUtil==null)
                                mRequestUtil = new MinuteRequest(ChartManager.this, symbol, gpInfo.getYesterday_price());
                            mRequestUtil.request();
                        }
                    });
                }catch (Exception e){
                }
            }
        }.start();
    }

    public void destroryRequest() {
        if (mRequestUtil != null)
            mRequestUtil.destoryRequest();
    }



    /*******************股票基本信息↓↓↓↓******************/
    /**顶部基本信息*/
    private void initView(){
        layout_baseinfo = rootView.findViewById(R.id.layout_baseinfo);
        tv_name = (TextView) layout_baseinfo.findViewById(R.id.tv_name);
        tv_code = (TextView) layout_baseinfo.findViewById(R.id.tv_code);
        tv_stop = (TextView) layout_baseinfo.findViewById(R.id.tv_stop);        //停牌
        tv_price = (TextView) layout_baseinfo.findViewById(R.id.tv_price);      //价格
        tv_price_add = (TextView) layout_baseinfo.findViewById(R.id.tv_price_add);   //涨值
        tv_price_addp = (TextView) layout_baseinfo.findViewById(R.id.tv_price_addp);    //涨幅
        tv_open = (TextView) layout_baseinfo.findViewById(R.id.tv_open);      //今开
        tv_heigh = (TextView) layout_baseinfo.findViewById(R.id.tv_heigh);    //最高
        tv_vol = (TextView) layout_baseinfo.findViewById(R.id.tv_vol);        //成交
        tv_low = (TextView) layout_baseinfo.findViewById(R.id.tv_low);        //最低
        tv_turnover = (TextView) layout_baseinfo.findViewById(R.id.tv_turnover);   //换手
        tv_amplitude = (TextView) layout_baseinfo.findViewById(R.id.tv_amplitude); //振幅
        ll_price = (LinearLayout) layout_baseinfo.findViewById(R.id.ll_price);

        layout_loading = rootView.findViewById(R.id.layout_loading);

        tv_junxian = (TextView) rootView.findViewById(R.id.tv_junxian);
        tv_shijian = (TextView) rootView.findViewById(R.id.tv_shijian);
        tv_fenshi = (TextView) rootView.findViewById(R.id.tv_fenshi);
        tv_fenshi.setTextColor(Constants.C_M_DATA_LINE);
        tv_junxian.setTextColor(Constants.C_M_AVG_LINE);
        tv_shijian.setTextColor(Constants.C_LABLE_TEXT);

        minuteHourView = (MinuteHourChart) rootView.findViewById(R.id.minutechart);
        barView = (MinuteBarChart) rootView.findViewById(R.id.barchart);
        focusView = (FocusChart)rootView.findViewById(R.id.focuschart);
        minuteHourView.setOnFocusChangeListener(foucsChangedListener);
        barView.setOnFocusChangeListener(foucsChangedListener);
        minuteHourView.setEnable(true);
        //盘口控件
        pankoulinear = (LinearLayout) rootView.findViewById(R.id.pankoulinear);
        pankouline = new FrameLayout(activity);
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,10);
        lp.gravity= Gravity.CENTER;
        pankouline.setLayoutParams(lp);

    }

    /**设置加载框可见*/
    public void setLoadViewVisibilty(int visibilty){
        layout_loading.setVisibility(visibilty);
    }

    /**展示基本信息*/
    public void showBaseInfo(StockBaseInfo gpInfo) {
        this.gpInfo = gpInfo;
        tv_name.setText(gpInfo.getName());
        tv_code.setText(gpInfo.getCode());
        try {
            int status = Integer.parseInt(gpInfo.getStatus());
            if (status == 2) {
                //股票状态，01：正常开始  02：停牌   04临时停牌  -1：未开市   -2：已收盘  -3：休市
                tv_stop.setVisibility(View.VISIBLE);
                ll_price.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }
        tv_open.setText("今开 " + gpInfo.getOpen());
        tv_heigh.setText("最高 " + gpInfo.getHigh());
        tv_low.setText("最低 " + gpInfo.getLow());
        tv_turnover.setText("换手 " + gpInfo.getHsl());
        tv_amplitude.setText("振幅 " + gpInfo.getZhenfu());

        updateBaseInfoLable(null, true);
    }
    /**根据分时数据更新成交量、价格、跌涨等信息*/
    private void updateBaseInfoLable(MinutesBean mb, boolean isCancel) {
        if (mb == null)
            mb = new MinutesBean();

        if (isCancel) {
            tv_vol.setText("成交 " + gpInfo.getVolume());
        } else {
            String[] vollables = CommonUtil.getDisplayVolume(String.valueOf(mb.cjnum).length(), mb.cjnum);
            tv_vol.setText("成交 " + vollables[0] + vollables[1]);
        }
        //价格、涨跌值、跌涨幅
        if(mb.cjprice == 0){
            tv_price_add.setTextColor(Color.DKGRAY);
            tv_price_addp.setTextColor(Color.DKGRAY);
            tv_price.setTextColor(Color.DKGRAY);
            tv_price.setText("--");
            tv_price_add.setText("--");
            tv_price_addp.setText("--");
        }else {
            tv_price.setText(Constants.twoPointFormat.format(mb.cjprice));
            float raiseValue = mb.cjprice - gpInfo.getYesterday_price();
            float raisePer = raiseValue / gpInfo.getYesterday_price();
            if (raiseValue > 0) {
                tv_price_add.setTextColor(Constants.C_RED_TEXT);
                tv_price_addp.setTextColor(Constants.C_RED_TEXT);
                tv_price.setTextColor(Constants.C_RED_TEXT);
                tv_price_add.setText("+"+ Constants.twoPointFormat.format(raiseValue));
                tv_price_addp.setText("+"+ Constants.twoPointFormat.format(raisePer * 100) + "%");
            } else if (raiseValue < 0) {
                tv_price_add.setTextColor(Constants.C_GREEN_TEXT);
                tv_price_addp.setTextColor(Constants.C_GREEN_TEXT);
                tv_price.setTextColor(Constants.C_GREEN_TEXT);
                tv_price_add.setText(Constants.twoPointFormat.format(raiseValue));
                tv_price_addp.setText(Constants.twoPointFormat.format(raisePer * 100) + "%");
            } else {
                tv_price_add.setTextColor(Color.DKGRAY);
                tv_price_addp.setTextColor(Color.DKGRAY);
                tv_price.setTextColor(Color.DKGRAY);
                tv_price_add.setText(Constants.twoPointFormat.format(raiseValue));
                tv_price_addp.setText(Constants.twoPointFormat.format(raisePer * 100) + "%");
            }
        }
    }

    /*******************股票基本信息↑↑↑↑******************/

    /*******************数据刷新↓↓↓↓******************/

    /**
     * 请求分数图数据成功后回调
     */
    public void setData(List<MinutesBean> list, MinuteParame parame) {
        this.datas = list;
        this.parame = parame;
        handler.sendEmptyMessage(Constants.MinutDataRefresh);
    }
    /**
     * 请求盘口数据成功后回调
     */
    public void setPankouData(PankouData.Data pankouData) {
        panKouDatas = pankouData;
        handler.sendEmptyMessage(Constants.PankouDataRefresh);
    }

    /**图标上方信息*/
    protected void updateTopLable(MinutesBean minutesBean){
        if(minutesBean==null)
            return;
        if (ll_lable == null) {
            ll_lable = (LinearLayout) rootView.findViewById(R.id.fenshilinear);
            ll_lable.setPadding(Constants.chartStart, 0,
                    CommonUtil.dip2px(activity,116), Constants.S_LABLE_CHART_DIS);
        }
        tv_fenshi.setText("分时: " + (minutesBean.cjprice==0 ? "--" : Constants.twoPointFormat.format(minutesBean.cjprice)));
        tv_junxian.setText("均线: " + (minutesBean.avprice==Float.NaN ? "--" : Constants.twoPointFormat.format(minutesBean.avprice)));
        tv_shijian.setText(minutesBean.time);
    }

    /**
     * 绑定盘口数据
     */
    public void updatePankouData(PankouData.Data pankouData){
        pankoulinear.setVisibility(View.VISIBLE);
        pankoulinear.removeAllViews();
        View view = null;
        if (pankouData == null)
            pankouData = new PankouData.Data();
        float prePrice = gpInfo.getYesterday_price();
        for (int i = 0; i < 10; i++) {
            switch (i) {
                case 0:
                    view = bondPankouData("卖" + 5,
                            Constants.twoPointFormat.format(pankouData.getS(4)),
                            pankouData.getSn(4)/100 + "",
                            Constants.C_LABLE_TEXT,
                            getColorPankou(prePrice,pankouData.getS(4)),
                            Constants.C_LABLE_TEXT);
                    break;
                case 1:
                    view = bondPankouData("卖" + 4,
                            Constants.twoPointFormat.format(pankouData.getS(3)),
                            pankouData.getSn(3)/100 + "",
                            Constants.C_LABLE_TEXT,
                            getColorPankou(prePrice,pankouData.getS(3)),
                            Constants.C_LABLE_TEXT);
                    break;
                case 2:
                    view = bondPankouData("卖" + 3,
                            Constants.twoPointFormat.format(pankouData.getS(2)),
                            pankouData.getSn(2)/100 + "",
                            Constants.C_LABLE_TEXT,
                            getColorPankou(prePrice,pankouData.getS(2)),
                            Constants.C_LABLE_TEXT);
                    break;
                case 3:
                    view = bondPankouData("卖" + 2,
                            Constants.twoPointFormat.format(pankouData.getS(1)),
                            pankouData.getSn(1)/100 + "",
                            Constants.C_LABLE_TEXT,
                            getColorPankou(prePrice,pankouData.getS(1)),
                            Constants.C_LABLE_TEXT);
                    break;
                case 4:
                    view = bondPankouData("卖" + 1,
                            Constants.twoPointFormat.format(pankouData.getS(0)),
                            pankouData.getSn(0)/100 + "",
                            Constants.C_LABLE_TEXT,
                            getColorPankou(prePrice,pankouData.getS(0)),
                            Constants.C_LABLE_TEXT);
                    break;
                case 5:
                    pankoulinear.addView(pankouline);

                    view = bondPankouData("买" + 1,
                            Constants.twoPointFormat.format(pankouData.getB(0)),
                            pankouData.getBn(0)/100 + "",
                            Constants.C_LABLE_TEXT,
                            getColorPankou(prePrice,pankouData.getB(0)),
                            Constants.C_LABLE_TEXT);

                    break;
                case 6:
                    view = bondPankouData("买" + 2,
                            Constants.twoPointFormat.format(pankouData.getB(1)),
                            pankouData.getBn(1)/100 + "",
                            Constants.C_LABLE_TEXT,
                            getColorPankou(prePrice,pankouData.getB(1)),
                            Constants.C_LABLE_TEXT);
                    break;
                case 7:
                    view = bondPankouData("买" + 3,
                            Constants.twoPointFormat.format(pankouData.getB(2)),
                            pankouData.getBn(2)/100 + "",
                            Constants.C_LABLE_TEXT,
                            getColorPankou(prePrice,pankouData.getB(2)),
                            Constants.C_LABLE_TEXT);
                    break;
                case 8:
                    view = bondPankouData("买" + 4,
                            Constants.twoPointFormat.format(pankouData.getB(3)),
                            pankouData.getBn(3)/100 + "",
                            Constants.C_LABLE_TEXT,
                            getColorPankou(prePrice,pankouData.getB(3)),
                            Constants.C_LABLE_TEXT);
                    break;
                case 9:
                    view = bondPankouData("买" + 5,
                            Constants.twoPointFormat.format(pankouData.getB(4)),
                            pankouData.getBn(4)/100 + "",
                            Constants.C_LABLE_TEXT,
                            getColorPankou(prePrice,pankouData.getB(4)),
                            Constants.C_LABLE_TEXT);
                    break;
            }
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,0,1));
            pankoulinear.addView(view);
        }
    }
    private int getColorPankou(float preprice,float price){
        if(price==0)
            return Constants.C_LABLE_TEXT;
        if(price > preprice)return Constants.C_RED_TEXT;
        if(price < preprice)return Constants.C_GREEN_TEXT;
        return Constants.C_LABLE_TEXT;
    }
    private View bondPankouData(String lable1, String lable2, String lable3, int c1, int c2, int c3){
        View view = LayoutInflater.from(activity).inflate(R.layout.itemb_ug_sell, null);
        ((TextView) view.findViewById(R.id.label1)).setText(lable1);
        ((TextView) view.findViewById(R.id.label2)).setText(lable2);
        ((TextView) view.findViewById(R.id.label3)).setText(lable3);
        ((TextView) view.findViewById(R.id.label1)).setTextColor(c1);
        ((TextView) view.findViewById(R.id.label2)).setTextColor(c2);
        ((TextView) view.findViewById(R.id.label3)).setTextColor(c3);
        return view;
    }

    /**盘口接口返回的换手率数据刷新顶部栏*/
    private void updateTopLableByPankou(PankouData.Data pankouData) {
        if (pankouData == null) return;
        gpInfo.setHsl(pankouData.hsl);
        gpInfo.setZhenfu(pankouData.zf);
        tv_turnover.setText("换手 " + gpInfo.getHsl());
        tv_amplitude.setText("振幅 " + gpInfo.getZhenfu());
    }

    /*******************数据刷新↑↑↑↑******************/


    /**焦点监听*/
    private TouchEventUtil.OnFoucsChangedListener foucsChangedListener =
            new TouchEventUtil.OnFoucsChangedListener() {
                @Override
                public void foucsChanged(int flag, int index) {
                    if (flag == Constants.FocusCancelFlag) {
                        handler.sendEmptyMessage(Constants.FocusCancelFlag);
                    } else {
                        if (index >= datas.size() || index < 0)
                            return;
                        final MinutesBean minutesBean = datas.get(index);
                        Message msg = handler.obtainMessage(Constants.FocusChangeFlag);
                        msg.obj = minutesBean;
                        handler.sendMessage(msg);
                    }
                }
            };

    public void updateFocusView(boolean cancelflag,MinutesBean minutesBean){
        if(cancelflag) {
            focusView.setCanceled(true);
            focusView.invalidate();
            updateTopLable(minutesBean);
        }else{
            focusView.setCanceled(false);
            if(!focusView.getFrameInit()) {
                focusView.setStart(Constants.chartStart);
                focusView.setFrame((int) minuteHourView.getLinewidth(), minuteHourView.getHeight() + barView.getHeight(),true);
            }else focusView.setIsLayout(false);
            focusView.update(minutesBean);
            updateTopLable(minutesBean);
        }
    }


















}
