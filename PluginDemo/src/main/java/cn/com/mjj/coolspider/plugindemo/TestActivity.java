package cn.com.mjj.coolspider.plugindemo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Mjj on 2017/5/2.
 */

public class TestActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button button = new Button(mProxyActivity);
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        button.setBackgroundColor(Color.YELLOW);
        button.setText("这是测试页面");
        setContentView(button);
    }

}
