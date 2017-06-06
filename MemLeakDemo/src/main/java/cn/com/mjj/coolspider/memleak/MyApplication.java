package cn.com.mjj.coolspider.memleak;

import android.app.Application;

/**
 * Created by Mike on 2017/4/19.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onCreate();
    }
}
