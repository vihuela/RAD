package worldgo.common.viewmodel.util;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IntDef;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.utils.SPUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;
import worldgo.common.viewmodel.app.BaseApplication;
import worldgo.common.viewmodel.util.rx.RxView;

import static android.content.Context.CLIPBOARD_SERVICE;


public class CommonUtils {
    private static final int Local = 0;
    private static final int Remote = 1;
    private final static ColorDrawable DEF_HOLDER = new ColorDrawable(Color.parseColor("#DCDDE1"));

    public static DrawableRequestBuilder getGlideStringBuilder(String url) {
        return Glide.with(BaseApplication.getAppContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(DEF_HOLDER)
                .crossFade();

    }

    public static DrawableRequestBuilder getGlideIntegerBuilder(int resId) {
        return Glide.with(BaseApplication.getAppContext()).load(resId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(DEF_HOLDER)
                .crossFade();

    }
    public static DrawableRequestBuilder getGlideStringBuilder(Context ctx,String url) {
        return Glide.with(ctx).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(DEF_HOLDER)
                .crossFade();

    }

    public static DrawableRequestBuilder getGlideIntegerBuilder(Context ctx,int resId) {
        return Glide.with(ctx).load(resId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(DEF_HOLDER)
                .crossFade();

    }

    /**
     * 图片价值
     * @param imageView
     * @param url 可以是网络url，也可以是本地资源id
     * @param transformation https://github.com/wasabeef/glide-transformations
     */
    public static void imageLoad(ImageView imageView, Object url, Transformation ...transformation) {

        imageLoad(BaseApplication.getAppContext().getCurActivity(),imageView, url,transformation);
    }

    @SuppressWarnings("all")
    private static void imageLoad(Context ctx, ImageView imageView, Object url, Transformation ...transformation) {

        int targetResId = 0;
        String targetUrl = null;

        try { targetResId = Integer.parseInt(url.toString());} catch (Exception ignore) {}
        try { targetUrl = String.valueOf(url);} catch (Exception ignore) {}
        if (targetResId != 0) {
            if(transformation.length>0){  getGlideIntegerBuilder(ctx,targetResId).bitmapTransform(transformation).into(imageView); }
            else{  getGlideIntegerBuilder(ctx,targetResId).into(imageView);  }
        } else if (!StringUtils.isEmpty(targetUrl)) {
            if(transformation.length>0){  getGlideStringBuilder(ctx,targetUrl).bitmapTransform(transformation).into(imageView); }
            else{  getGlideStringBuilder(ctx,targetUrl).into(imageView);  }
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
     * 剪辑版
     */
    public static void copyTextToClipboard(Context context, CharSequence text) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", text);
        manager.setPrimaryClip(clipData);
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
