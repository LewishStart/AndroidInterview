package cn.com.mjj.coolspider.intentservicedemo;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * author: sundong
 * created at 2017/7/11 10:39
 */

public class MyIntentService extends IntentService {
    public static final String DOWNLOAD_URL = "download_url";
    public static final String INDEX_FLAG = "index_flag";
    public Handler dispatcher = new Handler(Looper.getMainLooper());
    public static DownLoadListener downLoadListener;


    public static void setDownLoadListener(DownLoadListener mDownLoadListener) {
        downLoadListener = mDownLoadListener;
    }

    public MyIntentService() {
        super("MyIntentService");
    }

    /**
     * 实现异步任务的方法
     *
     * @param intent Activity传递过来的Intent,数据封装在intent中
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            final Bitmap bitmap = downloadUrlBitmap(intent.getIntExtra(INDEX_FLAG, 0), intent.getStringExtra(DOWNLOAD_URL), downLoadListener);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    private Bitmap downloadUrlBitmap(final int index, String urlString, final DownLoadListener downLoadListener) throws InterruptedException {
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;
        Bitmap bitmap = null;
        dispatcher.post(new Runnable() {
            @Override
            public void run() {
                if (downLoadListener != null) {
                    downLoadListener.onDownLoadStart(index);
                }
            }
        });
        Thread.sleep(2000);
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        Thread.sleep(2000);
        final Bitmap finalBitmap = bitmap;
        dispatcher.post(new Runnable() {
            @Override
            public void run() {
                if (downLoadListener != null) {
                    downLoadListener.onDownLoadFinished(index, finalBitmap);
                }
            }
        });
        return bitmap;
    }

    public interface DownLoadListener {
        void onDownLoadStart(int index);

        void onDownLoadProgress(int index);

        void onDownLoadFinished(int index, Bitmap bitmap);
    }
}
