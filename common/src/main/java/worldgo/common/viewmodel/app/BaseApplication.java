package worldgo.common.viewmodel.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.utils.AppUtils;
import com.blankj.utilcode.utils.Utils;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.util.Stack;

import worldgo.common.viewmodel.util.CommonUtils;

public abstract class BaseApplication extends Application {
    private static BaseApplication baseApplication;
    public Stack<Activity> store;

    public static BaseApplication getAppContext() {
        return baseApplication;
    }


    public static Resources getAppResources() {
        return baseApplication.getResources();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        if (CommonUtils.isMainProcess(this)) {
            store = new Stack<>();
            registerActivityLifecycleCallbacks(new SwitchBackgroundCallbacks());
            //utils
            Utils.init(this);
            //log
            Logger
                    .init(AppUtils.getAppName(this))
                    .methodCount(0)
                    .hideThreadInfo()
                    .logLevel(isDebug() ? LogLevel.FULL : LogLevel.NONE)
                    .methodOffset(0);
            onMainProcessCreate();
        }


    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public abstract boolean isDebug();
    public abstract void onMainProcessCreate();

    /**
     * 获取当前的Activity
     *
     * @return
     */
    public Activity getCurActivity() {
        return store.lastElement();
    }

    private class SwitchBackgroundCallbacks implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            store.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            store.remove(activity);
        }
    }
}
