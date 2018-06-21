package com.game.db.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类需要使用代理会通过此注释，注入到代理对象的变化集合里
 * @author JiangBangMing
 *
 * 2018年6月20日 上午11:52:27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodSaveProxy {

	String proxy();
}
