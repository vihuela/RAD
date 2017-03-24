package worldgo.common.viewmodel.util;

import android.app.ActivityManager;
import android.content.Context;
import android.view.View;

import com.blankj.utilcode.utils.SPUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;
import worldgo.common.viewmodel.util.rx.RxView;


public class CommonUtils {

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
        if (Preconditions.isNotBlank(runningApps) ) {
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
    public static void loginEnable(boolean isLogin){
        SPUtils spUtils=new SPUtils("user_status");
        spUtils.putBoolean("user_login",isLogin);
    }

}
