package com.game.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

import com.game.common.enums.BOEnum;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月13日 下午2:02:46
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcServiceBoEnum {

	BOEnum bo();
}
