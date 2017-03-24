package worldgo.common.viewmodel.aop.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import worldgo.common.viewmodel.util.CommonUtils;

/**
 * 功能：检查用户是否登陆，未登录则提示登录，不会执行下面的逻辑
 * 业务端务必调用设置登录态 ：{@link CommonUtils#loginEnable(boolean)}
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface CheckLogin {
}
