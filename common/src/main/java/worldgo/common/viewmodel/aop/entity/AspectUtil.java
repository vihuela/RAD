package worldgo.common.viewmodel.aop.entity;

import com.orhanobut.logger.Logger;
import com.orhanobut.logger.Printer;

import org.aspectj.lang.ProceedingJoinPoint;


public class AspectUtil {
    public static String getClassName(final ProceedingJoinPoint joinPoint) {
        return joinPoint.getSignature().getDeclaringType().getSimpleName();
    }

    public static Printer getLogger(final ProceedingJoinPoint joinPoint) {
        return Logger.t(AspectUtil.getClassName(joinPoint), 0);
    }

    public static void print(final ProceedingJoinPoint joinPoint, String log) {
        Logger.t(AspectUtil.getClassName(joinPoint), 0).w(log);
    }
}
