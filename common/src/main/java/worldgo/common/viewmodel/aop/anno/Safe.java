package worldgo.common.viewmodel.aop.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;


/**
 * 无视方法抛出异常
 */
@Target({METHOD})
@Retention(CLASS)
public @interface Safe {
}
