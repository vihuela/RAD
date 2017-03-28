package worldgo.common.viewmodel.util;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.utils.SPUtils;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;
import worldgo.common.viewmodel.app.BaseApplication;
import worldgo.common.viewmodel.util.rx.RxView;


public class CommonUtils {
    private final static ColorDrawable DEF_HOLDER = new ColorDrawable(Color.parseColor("#DCDDE1"));

    public static DrawableRequestBuilder getGlideBuilder(String url) {
        return Glide.with(BaseApplication.getAppContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(DEF_HOLDER)
                .centerCrop()
                .crossFade();

    }

    /**
     * 图片加载
     */
    public static void imageLoad(ImageView imageView, String url) {
        getGlideBuilder(url).into(imageView);
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

}
