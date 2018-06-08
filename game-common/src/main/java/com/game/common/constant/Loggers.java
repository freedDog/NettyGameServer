package com.game.common.constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 统一定义系统使用slf4j的Logger
 * @author JiangBangMing
 *
 * 2018年5月31日 下午7:34:31
 */
public class Loggers {
	/**Server相关的日志*/
	public static final Logger serverLogger=LoggerFactory.getLogger("server");
	/**Game Server 相关的日志*/
	public static final Logger gameLogger=LoggerFactory.getLogger("game");
	/**error 相关的日志*/
	public static final Logger errorLogger=LoggerFactory.getLogger("error");
	/**session相关的日志*/
	public static final Logger sessionLogger=LoggerFactory.getLogger("session");
	/**服务器状态统计*/
	public static final Logger serverStatusStatistics=LoggerFactory.getLogger("statistics");
	/**消息处理相关的日志*/
	public static final Logger msgLogger=LoggerFactory.getLogger("msg");
	/**Game Server 相关的日志*/
	public static final Logger handerLogger=LoggerFactory.getLogger("handler");
	/**admin相关的日志*/
	public static final Logger adminLogger=LoggerFactory.getLogger("admin");
	
	
}
