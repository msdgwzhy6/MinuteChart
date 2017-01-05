package com.openxu.minutechart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.openxu.minutechart.manager.ChartManager;

public class MainActivity extends AppCompatActivity {
    private ChartManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化股票图表管理器，参数为当前activity对象、股票代码
        manager=new ChartManager(this, "sz000002");
        manager.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.destroryRequest();
    }
}
