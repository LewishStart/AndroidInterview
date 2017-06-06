package cn.com.mjj.coolspider.plugindemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

import dalvik.system.DexClassLoader;

/**
 * Created by Mjj on 2017/5/2.
 */

public class ProxyActivity extends Activity {

    private static final String TAG = "ProxyActivity";

    public static final String FROM = "extra.from";
    public static final int FROM_EXTERNAL = 0;
    public static final int FROM_INTERNAL = 1;

    public static final String EXTRA_DEX_PATH = "extra.dex.path";
    public static final String EXTRA_CLASS = "extra.class";

    private String mClass;
    private String mDexPath;
    private Resources mResources;
    private AssetManager mAssetManager;
    private Resources.Theme mTheme;
    private HashMap mActivityLifecircleMethods = new HashMap();
    private Activity mRemoteActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDexPath = getIntent().getStringExtra(EXTRA_DEX_PATH);
        mClass = getIntent().getStringExtra(EXTRA_CLASS);

        Log.d(TAG, "mClass=" + mClass + " mDexPath=" + mDexPath);
        if (mClass == null) {
            launchTargetActivity();
        } else {
            launchTargetActivity(mClass);
        }
    }

    @SuppressLint("NewApi")
    protected void launchTargetActivity() {
        PackageInfo packageInfo = getPackageManager().getPackageArchiveInfo(
                mDexPath, 1);
        if ((packageInfo.activities != null)
                && (packageInfo.activities.length > 0)) {
            String activityName = packageInfo.activities[0].name;
            mClass = activityName;
            launchTargetActivity(mClass);
        }
    }

    @SuppressLint("NewApi")
    protected void launchTargetActivity(final String className) {
        Log.d(TAG, "start launchTargetActivity, className=" + className);
        File dexOutputDir = this.getDir("dex", 0);
        final String dexOutputPath = dexOutputDir.getAbsolutePath();
        ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
        DexClassLoader dexClassLoader = new DexClassLoader(mDexPath,
                dexOutputPath, null, localClassLoader);
        try {
            Class<?> localClass = dexClassLoader.loadClass(className);
            Constructor<?> localConstructor = localClass
                    .getConstructor(new Class[] {});
            Object instance = localConstructor.newInstance(new Object[] {});
            Log.d(TAG, "instance = " + instance);

            Method setProxy = localClass.getMethod("setProxy",
                    new Class[] { Activity.class });
            setProxy.setAccessible(true);
            setProxy.invoke(instance, new Object[] { this });

            Method onCreate = localClass.getDeclaredMethod("onCreate",
                    new Class[] { Bundle.class });
            onCreate.setAccessible(true);
            Bundle bundle = new Bundle();
            bundle.putInt(FROM, FROM_EXTERNAL);
            onCreate.invoke(instance, new Object[] { bundle });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadResources() {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, mDexPath);
            mAssetManager = assetManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Resources superRes = super.getResources();
        mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(),
                superRes.getConfiguration());
        mTheme = mResources.newTheme();
        mTheme.setTo(super.getTheme());
    }

    protected void instantiateLifecircleMethods(Class<?> localClass) {
        String[] methodNames = new String[] {
                "onRestart",
                "onStart",
                "onResume",
                "onPause",
                "onStop",
                "onDestory"
        };
        for (String methodName : methodNames) {
            Method method = null;
            try {
                method = localClass.getDeclaredMethod(methodName, new Class[] { });
                method.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            mActivityLifecircleMethods.put(methodName, method);
        }

        Method onCreate = null;
        try {
            onCreate = localClass.getDeclaredMethod("onCreate", new Class[] { Bundle.class });
            onCreate.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        mActivityLifecircleMethods.put("onCreate", onCreate);

        Method onActivityResult = null;
        try {
            onActivityResult = localClass.getDeclaredMethod("onActivityResult",
                    new Class[] { int.class, int.class, Intent.class });
            onActivityResult.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        mActivityLifecircleMethods.put("onActivityResult", onActivityResult);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Method onResume = (Method) mActivityLifecircleMethods.get("onResume");
        if (onResume != null) {
            try {
                onResume.invoke(mRemoteActivity, new Object[] { });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        Method onPause = (Method) mActivityLifecircleMethods.get("onPause");
        if (onPause != null) {
            try {
                onPause.invoke(mRemoteActivity, new Object[] { });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onPause();
    }

}
