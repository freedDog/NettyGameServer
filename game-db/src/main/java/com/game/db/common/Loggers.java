package com.game.db.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月20日 上午11:45:02
 */
public class Loggers {
    /** 数据库相关的日志 */
    public static final Logger dbLogger = LoggerFactory.getLogger("db");
    /** 数据库启动的日志 */
    public static final Logger dbServerLogger = LoggerFactory.getLogger("dbServer");

    /** 数据实体代理的日志 */
    public static final Logger dbProxyLogger = LoggerFactory.getLogger("dbProxy");
    /** 数据实体服务代理的日志 */
    public static final Logger dbServiceProxyLogger = LoggerFactory.getLogger("dbServiceProxy");

    /** 数据实体存储异常的日志 */
    public static final Logger dbErrorLogger = LoggerFactory.getLogger("dbError");
	
}
