package com.openxu.minutechart.bean;

import java.io.Serializable;

/**
 * author : openXu
 * create at : 2016/07/8 13:40
 * blog : http://blog.csdn.net/xmxkf
 * gitHub : https://github.com/openXu
 * project : StockChart
 * class name : StockBaseInfo
 * version : 1.0
 * class describe：股票基本信息
 */
public class StockBaseInfo implements Serializable{

    /**
     * symbol :
     * code :
     * name :
     * open : 0.00
     * yesterday_price : 0.00
     * trade : 0.00
     * high : 0.00
     * low : 0.00
     * updownvalue : 0.00
     * updownrate : 0.00%
     * buy : 0.00
     * sell : 0.00
     * buy1_amount :
     * buy1_price : 0.00
     * buy2_amount : null
     * buy2_price : 0.00
     * buy3_amount : null
     * buy3_price : 0.00
     * buy4_amount : null
     * buy4_price : 0.00
     * buy5_amount : null
     * buy5_price : 0.00
     * sell1_amount : null
     * sell1_price : 0.00
     * sell2_amount : null
     * sell2_price : 0.00
     * sell3_amount : null
     * sell3_price : 0.00
     * sell4_amount : null
     * sell4_price : 0.00
     * sell5_amount : null
     * sell5_price : 0.00
     * dt :
     * time :
     * status : -1
     * volume : 0
     * amount :
     * hsl : 5.36%
     * syl : 865.36
     * zhenfu : 62.54%
     * junjia : 365.58
     * zongshizhi : 685.26亿
     * liutongzhi : 685.26亿
     * fenshi : http://image.sinajs.cn/newchart/min/n/300079.gif
     * day_k : http://image.sinajs.cn/newchart/daily/n/300079.gif
     * week_k : http://image.sinajs.cn/newchart/weekly/n/300079.gif
     * month_k : http://image.sinajs.cn/newchart/monthly/n/300079.gif
     * zixuan_status : no
     */

    private String symbol;       //股票代码（带前缀） sz000892
    private String code;         //股票代码         000892
    private String name;         //名称            星美联合
    private String type;         //1:股票   2:指数
    private float open;          //今开            15.81
    private float yesterday_price;//昨收   14.37
    private float high;         //最高     15.81
    private float low;          //最低     15.81
    private float trade;        //现价     15.81
    private String updownrate;   //跌涨幅    +10.02%
    private double updownvalue;  //涨跌值    +1.44
    private String amount;        //成交额    2707.56万
    private String volume;        //成交量     1.71万
    private String zhenfu;        //振幅    62.54%
    private String hsl;           //换手率5.36%
    private String syl;           //市盈率     865.36
    private String junjia;         //均价   365.58
    private String liutongzhi;     //流通市值  685.26亿
    private String zongshizhi;     //总市值   685.26亿
    private String dt;              //时间    2016-07-22
    private String time;             //14:24:36
    private String zixuan_status;  //是否已添加自选 0未添加自选，1已添加自选
    private String status;        //股票状态，01：正常开始  02：停牌   04临时停牌  -1：未开市   -2：已收盘  -3：休市
    
    private String buy;          //买一价    15.81
    private String sell;         //卖一价    0.00
    private String buy1_amount;  //买一      10022153
    private String buy1_price;   //买一价    15.81
    private String buy2_amount;  //买二      45100
    private String buy2_price;   //买二价     15.80
    private String buy3_amount;  //买三      1900
    private String buy3_price;   //买三价    15.79
    private String buy4_amount;
    private String buy4_price;
    private String buy5_amount;
    private String buy5_price;
    private String sell1_amount;
    private String sell1_price;
    private String sell2_amount;
    private String sell2_price;
    private String sell3_amount;
    private String sell3_price;
    private String sell4_amount;
    private String sell4_price;
    private String sell5_amount;
    private String sell5_price;
    private String fenshi;         //
    private String day_k;
    private String week_k;
    private String month_k;
    private String news_url;
    private String jscript_text;
    private String fenshi_jump_url;
    private String day_k_jump_url;
    private String week_k_jump_url;
    private String month_k_jump_url;



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFenshi_jump_url() {
        return fenshi_jump_url;
    }

    public void setFenshi_jump_url(String fenshi_jump_url) {
        this.fenshi_jump_url = fenshi_jump_url;
    }

    public String getDay_k_jump_url() {
        return day_k_jump_url;
    }

    public void setDay_k_jump_url(String day_k_jump_url) {
        this.day_k_jump_url = day_k_jump_url;
    }

    public String getWeek_k_jump_url() {
        return week_k_jump_url;
    }

    public void setWeek_k_jump_url(String week_k_jump_url) {
        this.week_k_jump_url = week_k_jump_url;
    }

    public String getMonth_k_jump_url() {
        return month_k_jump_url;
    }

    public void setMonth_k_jump_url(String month_k_jump_url) {
        this.month_k_jump_url = month_k_jump_url;
    }

    public String getNews_url() {
        return news_url;
    }

    public void setNews_url(String news_url) {
        this.news_url = news_url;
    }

    public String getJscript_text() {
        return jscript_text;
    }

    public void setJscript_text(String jscript_text) {
        this.jscript_text = jscript_text;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getYesterday_price() {
        return yesterday_price;
    }

    public void setYesterday_price(float yesterday_price) {
        this.yesterday_price = yesterday_price;
    }

    public float getTrade() {
        return trade;
    }

    public void setTrade(float trade) {
        this.trade = trade;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public double getUpdownvalue() {
        return updownvalue;
    }

    public void setUpdownvalue(double updownvalue) {
        this.updownvalue = updownvalue;
    }

    public String getUpdownrate() {
        return updownrate;
    }

    public void setUpdownrate(String updownrate) {
        this.updownrate = updownrate;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getBuy1_amount() {
        return buy1_amount;
    }

    public void setBuy1_amount(String buy1_amount) {
        this.buy1_amount = buy1_amount;
    }

    public String getBuy1_price() {
        return buy1_price;
    }

    public void setBuy1_price(String buy1_price) {
        this.buy1_price = buy1_price;
    }

    public String getBuy2_amount() {
        return buy2_amount;
    }

    public void setBuy2_amount(String buy2_amount) {
        this.buy2_amount = buy2_amount;
    }

    public String getBuy2_price() {
        return buy2_price;
    }

    public void setBuy2_price(String buy2_price) {
        this.buy2_price = buy2_price;
    }

    public String getBuy3_amount() {
        return buy3_amount;
    }

    public void setBuy3_amount(String buy3_amount) {
        this.buy3_amount = buy3_amount;
    }

    public String getBuy3_price() {
        return buy3_price;
    }

    public void setBuy3_price(String buy3_price) {
        this.buy3_price = buy3_price;
    }

    public String getBuy4_amount() {
        return buy4_amount;
    }

    public void setBuy4_amount(String buy4_amount) {
        this.buy4_amount = buy4_amount;
    }

    public String getBuy4_price() {
        return buy4_price;
    }

    public void setBuy4_price(String buy4_price) {
        this.buy4_price = buy4_price;
    }

    public String getBuy5_amount() {
        return buy5_amount;
    }

    public void setBuy5_amount(String buy5_amount) {
        this.buy5_amount = buy5_amount;
    }

    public String getBuy5_price() {
        return buy5_price;
    }

    public void setBuy5_price(String buy5_price) {
        this.buy5_price = buy5_price;
    }

    public String getSell1_amount() {
        return sell1_amount;
    }

    public void setSell1_amount(String sell1_amount) {
        this.sell1_amount = sell1_amount;
    }

    public String getSell1_price() {
        return sell1_price;
    }

    public void setSell1_price(String sell1_price) {
        this.sell1_price = sell1_price;
    }

    public String getSell2_amount() {
        return sell2_amount;
    }

    public void setSell2_amount(String sell2_amount) {
        this.sell2_amount = sell2_amount;
    }

    public String getSell2_price() {
        return sell2_price;
    }

    public void setSell2_price(String sell2_price) {
        this.sell2_price = sell2_price;
    }

    public String getSell3_amount() {
        return sell3_amount;
    }

    public void setSell3_amount(String sell3_amount) {
        this.sell3_amount = sell3_amount;
    }

    public String getSell3_price() {
        return sell3_price;
    }

    public void setSell3_price(String sell3_price) {
        this.sell3_price = sell3_price;
    }

    public String getSell4_amount() {
        return sell4_amount;
    }

    public void setSell4_amount(String sell4_amount) {
        this.sell4_amount = sell4_amount;
    }

    public String getSell4_price() {
        return sell4_price;
    }

    public void setSell4_price(String sell4_price) {
        this.sell4_price = sell4_price;
    }

    public String getSell5_amount() {
        return sell5_amount;
    }

    public void setSell5_amount(String sell5_amount) {
        this.sell5_amount = sell5_amount;
    }

    public String getSell5_price() {
        return sell5_price;
    }

    public void setSell5_price(String sell5_price) {
        this.sell5_price = sell5_price;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getHsl() {
        return hsl==null ? "--" : hsl;
    }

    public void setHsl(String hsl) {
        this.hsl = hsl;
    }

    public String getSyl() {
        return syl;
    }

    public void setSyl(String syl) {
        this.syl = syl;
    }

    public String getZhenfu() {
        return zhenfu==null ? "0" : zhenfu;
    }

    public void setZhenfu(String zhenfu) {
        this.zhenfu = zhenfu;
    }

    public String getJunjia() {
        return junjia;
    }

    public void setJunjia(String junjia) {
        this.junjia = junjia;
    }

    public String getZongshizhi() {
        return zongshizhi;
    }

    public void setZongshizhi(String zongshizhi) {
        this.zongshizhi = zongshizhi;
    }

    public String getLiutongzhi() {
        return liutongzhi;
    }

    public void setLiutongzhi(String liutongzhi) {
        this.liutongzhi = liutongzhi;
    }

    public String getFenshi() {
        return fenshi;
    }

    public void setFenshi(String fenshi) {
        this.fenshi = fenshi;
    }

    public String getDay_k() {
        return day_k;
    }

    public void setDay_k(String day_k) {
        this.day_k = day_k;
    }

    public String getWeek_k() {
        return week_k;
    }

    public void setWeek_k(String week_k) {
        this.week_k = week_k;
    }

    public String getMonth_k() {
        return month_k;
    }

    public void setMonth_k(String month_k) {
        this.month_k = month_k;
    }

    public String getZixuan_status() {
        return zixuan_status;
    }

    public void setZixuan_status(String zixuan_status) {
        this.zixuan_status = zixuan_status;
    }
}

