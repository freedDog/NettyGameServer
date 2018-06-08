package com.game.message.handler;
/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月5日 下午9:49:02
 */

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;

import com.game.common.annotation.MessageCommandAnnotation;
import com.game.common.constant.Loggers;

public abstract class AbstractMessageHandler implements IMessageHandler{

	public static final Logger logger=Loggers.gameLogger;
	
	private Map<Integer, Method> handlerMethods;
	
	public AbstractMessageHandler() {
		init();
	}
	public void init() {
		handlerMethods=new HashMap<>();
		Method[] methods=this.getClass().getMethods();
		for(Method method:methods) {
			if(method.isAnnotationPresent(MessageCommandAnnotation.class)) {
				MessageCommandAnnotation messageCommandAnnotation=method.getAnnotation(MessageCommandAnnotation.class);
				if(messageCommandAnnotation!=null) {
					handlerMethods.put(messageCommandAnnotation.command(), method);
				}
			}
		}
	}
	
	public Method getMessageHandler(int op) {
		return handlerMethods.get(op);
	}
	
	public Map<Integer, Method> getHandlerMethods() {
		return handlerMethods;
	}
	public void setHandlerMethods(Map<Integer, Method> handlerMethods) {
		this.handlerMethods = handlerMethods;
	}
	
}
