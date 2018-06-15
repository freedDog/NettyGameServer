package com.game.service.rpc.client.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.slf4j.Logger;

import com.game.common.constant.Loggers;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月12日 下午6:18:28
 */
public class ObjectProxy<T> implements InvocationHandler{

	private Logger logger=Loggers.rpcLogger;
	private Class<T> clazz;
	private int timeOut;
	
	public ObjectProxy(Class<T> clazz,int timeOut) {
		this.clazz=clazz;
		this.timeOut=timeOut;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		return null;
	}

}
