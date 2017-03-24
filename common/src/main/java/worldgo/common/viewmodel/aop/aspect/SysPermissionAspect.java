package worldgo.common.viewmodel.aop.aspect;

import android.support.v7.app.AppCompatActivity;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

import worldgo.common.viewmodel.aop.anno.Permission;
import worldgo.common.viewmodel.aop.entity.MPermissionUtils;
import worldgo.common.viewmodel.app.BaseApplication;

@Aspect
public class SysPermissionAspect {
    @Around("execution(!synthetic * *(..)) && onPermissionMethod()")
    public void doonPermissionMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        aroundJoinPoint(joinPoint);
    }

    @Pointcut("@within(worldgo.common.viewmodel.aop.anno.Permission)||@annotation(worldgo.common.viewmodel.aop.anno.Permission)")
    public void onPermissionMethod() {
    }

    private void aroundJoinPoint(final ProceedingJoinPoint joinPoint) throws Throwable {
        final AppCompatActivity ac = (AppCompatActivity) BaseApplication.getAppContext().getCurActivity();

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        Permission permission = method.getAnnotation(Permission.class);

        if (permission == null) return;

        MPermissionUtils.requestPermissionsResult(ac, 1, permission.value()
                , new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        try {
                            joinPoint.proceed();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onPermissionDenied() {
                        MPermissionUtils.showTipsDialog(ac);
                    }
                });
    }
}


