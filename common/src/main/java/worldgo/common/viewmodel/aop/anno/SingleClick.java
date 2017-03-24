package worldgo.common.viewmodel.aop.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import worldgo.common.viewmodel.util.CommonUtils;

/**
 * 防止View被连续点击，代码设置请用，{@link CommonUtils#singleClick(android.view.View, android.view.View.OnClickListener)}
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface SingleClick {
}
