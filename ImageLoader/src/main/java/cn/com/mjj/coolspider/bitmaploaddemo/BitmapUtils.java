package cn.com.mjj.coolspider.bitmaploaddemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Mjj on 2017/5/2.
 */

public class BitmapUtils {
    // 根据maxWidth, maxHeight计算最合适的inSampleSize
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }
        return inSampleSize;
    }

    //缩略图
    public static Bitmap thumbnail(String path,
                                   int maxWidth, int maxHeight, boolean autoRotate) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高信息到options中, 此时返回bm为空
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        // 计算缩放比
        int sampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inSampleSize = sampleSize;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

    //保存bitmap
    public static String save(Bitmap bitmap,
                               Bitmap.CompressFormat format, int quality, File destFile) {
        try {
            FileOutputStream out = new FileOutputStream(destFile);
            if (bitmap.compress(format, quality, out)) {
                out.flush();
                out.close();
            }
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            return destFile.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 保存到sd卡
    public static String save(Bitmap bitmap,
                               Bitmap.CompressFormat format, int quality, Context context) {
        if (!Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        File dir = new File(Environment.getExternalStorageDirectory()
                + "/" + context.getPackageName() + "/save/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File destFile = new File(dir, UUID.randomUUID().toString());
        return save(bitmap, format, quality, destFile);
    }
}
