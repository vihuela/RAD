package worldgo.common.viewmodel.aop.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;


/**
 * 打印方法耗时
 */
@Target({METHOD})
@Retention(CLASS)
public @interface Trace {
}
