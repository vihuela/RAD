package worldgo.common.viewmodel.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

import worldgo.common.viewmodel.aop.anno.HookMethod;
import worldgo.common.viewmodel.aop.entity.AspectUtil;
import worldgo.common.viewmodel.util.Preconditions;
import worldgo.common.viewmodel.util.reflect.Reflect;
import worldgo.common.viewmodel.util.reflect.ReflectException;


@Aspect
public class HookMethodAspect {

    @Around("execution(!synthetic * *(..)) && onHookMethod()")
    public void doHookMethodd(final ProceedingJoinPoint joinPoint) throws Throwable {
        hookMethod(joinPoint);
    }

    @Pointcut("@within(worldgo.common.viewmodel.aop.anno.HookMethod)||@annotation(worldgo.common.viewmodel.aop.anno.HookMethod)")
    public void onHookMethod() {
    }

    private void hookMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        HookMethod hookMethod = method.getAnnotation(HookMethod.class);

        if (hookMethod == null) return;

        String beforeMethod = hookMethod.beforeMethod();
        String afterMethod = hookMethod.afterMethod();

        if (Preconditions.isNotBlank(beforeMethod)) {
            try {
                Reflect.on(joinPoint.getTarget()).call(beforeMethod);
            } catch (ReflectException e) {
                e.printStackTrace();
                AspectUtil.print(joinPoint, "no method " + beforeMethod);
            }
        }

        joinPoint.proceed();

        if (Preconditions.isNotBlank(afterMethod)) {
            try {
                Reflect.on(joinPoint.getTarget()).call(afterMethod);
            } catch (ReflectException e) {
                e.printStackTrace();
                AspectUtil.print(joinPoint, "no method " + afterMethod);
            }
        }
    }
}
