package cn.com.mjj.coolspider.intentservicedemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    /**
     * 图片地址集合
     */
    private String url[] = {
            "http://pic6.huitu.com/res/20130116/84481_20130116142820494200_1.jpg",
            "http://sucai.qqjay.com/qqjayxiaowo/201210/26/1.jpg",
            "http://img02.tooopen.com/images/20140504/sy_60294738471.jpg",
            "http://img2.imgtn.bdimg.com/it/u=819201812,3553302270&fm=214&gp=0.jpg",
            "http://pic55.nipic.com/file/20141208/19462408_171130083000_2.jpg",
            "http://img05.tooopen.com/images/20150531/tooopen_sy_127457023651.jpg",
            "http://pic.58pic.com/58pic/17/41/38/88658PICNuP_1024.jpg"
    };

    private static ImageView imageView;
    private static final Handler mUIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            imageView.setImageBitmap((Bitmap) msg.obj);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image);
        MyIntentService.setDownLoadListener(new MyIntentService.DownLoadListener() {
            @Override
            public void onDownLoadStart(int index) {
                Toast.makeText(MainActivity.this, "第"+index+"张开始下载", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownLoadProgress(int index) {

            }

            @Override
            public void onDownLoadFinished(int index, Bitmap bitmap) {
                Toast.makeText(MainActivity.this, "第"+index+"张下载完成", Toast.LENGTH_SHORT).show();
                imageView.setImageBitmap(bitmap);
            }
        });
        Intent intent = new Intent(this, MyIntentService.class);
        for (int i = 0; i < 7; i++) {
            intent.putExtra(MyIntentService.DOWNLOAD_URL, url[i]);
            intent.putExtra(MyIntentService.INDEX_FLAG, i);
            startService(intent);
        }
    }
}
