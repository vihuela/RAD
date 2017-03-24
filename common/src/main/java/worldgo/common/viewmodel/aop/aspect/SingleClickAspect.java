package worldgo.common.viewmodel.aop.aspect;

import android.view.View;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Calendar;

import worldgo.common.R;


@Aspect
public class SingleClickAspect {
    private static final int MIN_CLICK_DELAY_TIME = 600;
    private static int TIME_TAG = R.id.click_time;

    @Pointcut("@within(worldgo.common.viewmodel.aop.anno.SingleClick)||@annotation(worldgo.common.viewmodel.aop.anno.SingleClick)")
    public void methodAnnotated() {
    }

    @Around("execution(!synthetic * *(..)) && methodAnnotated()")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        View view = null;
        for (Object arg : joinPoint.getArgs())
            if (arg instanceof View) view = (View) arg;
        if (view != null) {
            Object tag = view.getTag(TIME_TAG);
            long lastClickTime = ((tag != null) ? (long) tag : 0);
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                view.setTag(TIME_TAG, currentTime);
                joinPoint.proceed();
            }
        }
    }
}
