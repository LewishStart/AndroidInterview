package com.mooc.www.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import cn.com.mjj.coolspider.myapplication.R;

/**
 * Created by Mike on 2017/4/8.
 */

public class ServiceActivity extends Activity implements View.OnClickListener {
    private Button mBtnStartService, mBtnStopService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        mBtnStartService = (Button) findViewById(R.id.btn_start_service);
        mBtnStopService = (Button) findViewById(R.id.btn_stop__service);

        mBtnStopService.setOnClickListener(this);
        mBtnStartService.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), FirstService.class);
        switch (v.getId()) {
            case R.id.btn_start_service:
                startService(intent);
                break;
            case R.id.btn_stop__service:
                stopService(intent);
                break;
            default:
                break;
        }
    }
}
