package com.game.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * 特殊事件监听器
 * @author JiangBangMing
 *
 * 2018年6月12日 下午12:39:23
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface SpecialEventListenerAnnotation {

	int listener();
}
