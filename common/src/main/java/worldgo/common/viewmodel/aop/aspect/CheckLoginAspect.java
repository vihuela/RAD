package worldgo.common.viewmodel.aop.aspect;

import android.view.View;

import com.blankj.utilcode.utils.SPUtils;
import com.blankj.utilcode.utils.SnackbarUtils;
import com.blankj.utilcode.utils.ToastUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import worldgo.common.viewmodel.app.BaseApplication;
import worldgo.common.viewmodel.util.CommonUtils;


/**
 * 功能：检查用户是否登陆，未登录则提示登录，不会执行下面的逻辑
 * 业务端务必调用 ：{@link CommonUtils#loginEnable(boolean)}
 */
@Aspect
public class CheckLoginAspect {
    @Around("execution(!synthetic * *(..)) && methodAnnotated()")
    public void doMethodAnnotated(final ProceedingJoinPoint joinPoint) throws Throwable {

        SPUtils spUtils = new SPUtils("user_status");
        if (!spUtils.getBoolean("user_login", false)) {
            SnackbarUtils.showShortSnackbar(
                    BaseApplication.getAppContext().getCurActivity().getWindow().getDecorView(),
                    "请先登录!",
                    0xFFFA7C20,
                    0xFF000000,
                    "登录",
                    0xFFFA7C20,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtils.showShortToast("执行登录跳转");
                        }
                    });
            return;
        }
        joinPoint.proceed();
    }


    @Pointcut("@within(worldgo.common.viewmodel.aop.anno.CheckLogin)||@annotation(worldgo.common.viewmodel.aop.anno.CheckLogin)")
    public void methodAnnotated() {
    }

}

