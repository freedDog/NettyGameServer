package com.game.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * 全局事件监听器注解
 * @author JiangBangMing
 *
 * 2018年6月12日 下午12:22:57
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface GlobalEventListenerAnnotation {

}
