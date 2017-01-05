package com.openxu.minutechart.bean;

/**
 * author : openXu
 * create at : 2016/07/8 13:40
 * blog : http://blog.csdn.net/xmxkf
 * gitHub : https://github.com/openXu
 * project : StockChart
 * class name : PankouData
 * version : 1.0
 * class describe：盘口数据
 */
public class PankouData {
    private Data data=null;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        public float b0=0;
        public int bn0=0;
        public float b1=0;
        public int bn1=0;
        public float b2=0;
        public int bn2=0;
        public float b3=0;
        public int bn3=0;
        public float b4=0;
        public int bn4=0;
        public float s0=0;
        public int sn0=0;
        public float s1=0;
        public int sn1=0;
        public float s2=0;
        public int sn2=0;
        public float s3=0;
        public int sn3=0;
        public float s4=0;
        public int sn4=0;
        public String hsl;
        public String zf;

        public float getB(int i) {
            switch (i) {
                case 0:
                    return b0;
                case 1:
                    return b1;
                case 2:
                    return b2;
                case 3:
                    return b3;
                case 4:
                    return b4;
                default:
                    return b0;
            }
        }

        public int getBn(int i) {
            switch (i) {
                case 0:
                    return bn0;
                case 1:
                    return bn1;
                case 2:
                    return bn2;
                case 3:
                    return bn3;
                case 4:
                    return bn4;
                default:
                    return bn0;
            }
        }

        public float getS(int i) {
            switch (i) {
                case 0:
                    return s0;
                case 1:
                    return s1;
                case 2:
                    return s2;
                case 3:
                    return s3;
                case 4:
                    return s4;
                default:
                    return s0;
            }
        }

        public int getSn(int i) {
            switch (i) {
                case 0:
                    return sn0;
                case 1:
                    return sn1;
                case 2:
                    return sn2;
                case 3:
                    return sn3;
                case 4:
                    return sn4;
                default:
                    return sn0;
            }
        }
    }

}
