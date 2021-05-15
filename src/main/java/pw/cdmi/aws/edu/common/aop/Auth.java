package pw.cdmi.aws.edu.common.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auth {

	/**
	 * true:表示此接口必须登录后才能访问
	 * false:表示不用登录,但是有可能需要获取用户信息
	 * @return
	 */
	boolean login() default true;
	
	/**
	 * 需要的角色权限
	 * @return
	 */
	String role() default "";
}
