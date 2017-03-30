package worldgo.common.viewmodel.util;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IntDef;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.utils.SPUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;
import worldgo.common.viewmodel.app.BaseApplication;
import worldgo.common.viewmodel.util.rx.RxView;


public class CommonUtils {
    public static final int Local = 0;
    public static final int Remote = 1;
    private final static ColorDrawable DEF_HOLDER = new ColorDrawable(Color.parseColor("#DCDDE1"));

    public static DrawableRequestBuilder getGlideStringBuilder(String url) {
        return Glide.with(BaseApplication.getAppContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(DEF_HOLDER)
                .centerCrop()
                .crossFade();

    }

    public static DrawableRequestBuilder getGlideIntegerBuilder(int resId) {
        return Glide.with(BaseApplication.getAppContext()).load(resId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(DEF_HOLDER)
                .centerCrop()
                .crossFade();

    }

    /**
     * 图片加载
     */
    public static void imageLoad(ImageView imageView, Object url) {

        int targetResId = 0;
        String targetUrl = null;

        try { targetResId = Integer.parseInt(url.toString());} catch (Exception ignore) {}
        try { targetUrl = String.valueOf(url);} catch (Exception ignore) {}
        if (targetResId != 0) {
            getGlideIntegerBuilder(targetResId).into(imageView);
        } else if (!StringUtils.isEmpty(targetUrl)) {
            getGlideStringBuilder(targetUrl).into(imageView);
        }
    }

    /**
     * 防止重复点击
     */
    public static void singleClick(final View v, final View.OnClickListener onClickListener) {
        RxView.clicks(v)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Object o) {
                        onClickListener.onClick(v);
                    }
                });
    }

    /**
     * 判断是否是主进程
     */
    public static boolean isMainProcess(Context cxt) {
        String pN = null;
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (Preconditions.isNotBlank(runningApps)) {
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo.pid == android.os.Process.myPid()) {
                    pN = procInfo.processName;
                    break;
                }
            }
        }

        return Preconditions.isNotBlank(pN) && pN.equals(cxt.getApplicationContext().getPackageName());
    }

    /**
     * 是否已登录，框架仅保存状态以便AOP调用
     */
    public static void loginEnable(boolean isLogin) {
        SPUtils spUtils = new SPUtils("user_status");
        spUtils.putBoolean("user_login", isLogin);
    }

    @IntDef(value = {Local, Remote})
    @Retention(RetentionPolicy.SOURCE)
    @interface ImageType {
    }

}
