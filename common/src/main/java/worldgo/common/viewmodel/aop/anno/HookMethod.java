package worldgo.common.viewmodel.aop.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;


/**
 * 在方法前后埋点
 */
@Target({METHOD})
@Retention(CLASS)
public @interface HookMethod {

    String beforeMethod() default "";

    String afterMethod() default "";
}
