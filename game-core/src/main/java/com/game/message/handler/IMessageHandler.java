package com.game.message.handler;
/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月1日 下午1:51:17
 */

import java.lang.reflect.Method;

public interface IMessageHandler {
	
	public Method getMessageHandler(int command);
}
