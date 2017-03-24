package worldgo.common.viewmodel.aop.anno;

/**
 * 申请系统权限注解
 * 敏感权限包括：
 * http://droidyue.com/blog/2016/01/17/understanding-marshmallow-runtime-permission/
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Permission {
    String[] value();
}