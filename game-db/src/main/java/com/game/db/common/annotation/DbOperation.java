package com.game.db.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.game.db.common.enums.DbOperationEnum;

/**
 * 数据存储操作
 * @author JiangBangMing
 *
 * 2018年6月20日 上午11:49:26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DbOperation {

	DbOperationEnum operation();
}
