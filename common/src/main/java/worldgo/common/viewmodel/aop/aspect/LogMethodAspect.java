package worldgo.common.viewmodel.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;

import worldgo.common.viewmodel.aop.entity.AspectUtil;

@Aspect
public class LogMethodAspect {

    private String argMessage;
    private String resMessage;
    private String from;

    @Around("execution(!synthetic * *(..)) && onLogMethod()")
    public Object doLogMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethod(joinPoint);
    }

    @Pointcut("@within(worldgo.common.viewmodel.aop.anno.LogMethod)||@annotation(worldgo.common.viewmodel.aop.anno.LogMethod)")
    public void onLogMethod() {
    }

    private Object logMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        from = joinPoint.getSignature().toShortString();
        argMessage = " Args : " + (joinPoint.getArgs() != null ? Arrays.deepToString(joinPoint.getArgs()) : "");
        String type = ((MethodSignature) joinPoint.getSignature()).getReturnType().toString();
        resMessage = " ，Result : " + ("void".equalsIgnoreCase(type) ? "void" : result);
        AspectUtil.print(joinPoint, from + "\n" + argMessage + resMessage);
        return result;
    }
}
