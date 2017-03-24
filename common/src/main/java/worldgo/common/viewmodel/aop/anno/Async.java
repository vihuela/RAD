package worldgo.common.viewmodel.aop.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;


/**
 * 异步执行方法
 */
@Target({METHOD})
@Retention(CLASS)
public @interface Async {
}
