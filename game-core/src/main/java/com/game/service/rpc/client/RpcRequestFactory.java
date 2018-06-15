package com.game.service.rpc.client;

import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.game.common.constant.Loggers;
import com.game.service.net.tcp.RpcRequest;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月13日 下午2:08:53
 */
@Service
public class RpcRequestFactory {

	public Logger logger=Loggers.rpcLogger;
	
	public RpcRequest createRequest(String className,String methodName,Object[] args) {
		RpcRequest request=new RpcRequest();
		request.setRequestId(UUID.randomUUID().toString());
		request.setClassName(className);
		request.setMethodName(methodName);
		request.setParameters(args);
		
		Class[] parameterTypes=new Class[args.length];
		
		for(int i=0;i<args.length;i++) {
			parameterTypes[i] =getClassType(args[i]);
		}
		request.setParameterTypes(parameterTypes);
		
		if(logger.isDebugEnabled()) {
			logger.debug(className);
			logger.debug(methodName);
			for(int i=0;i<parameterTypes.length;i++) {
				logger.debug(parameterTypes[i].getName());
			}
			for(int i=0;i<args.length;i++) {
				logger.debug(args[i].toString());
			}
		}
		return request;
	}
	
	private Class<?> getClassType(Object obj){
		Class<?> classType=obj.getClass();
		String typeName=classType.getName();
		switch (typeName) {
		case "java.lang.Integer":
			return Integer.TYPE;
		case "java.lang.Long":
			return Long.TYPE;
		case "java.lang.Float":
			return Float.TYPE;
		case "java.lang.Double":
			return Double.TYPE;
		case "java.lang.Character":
			return Character.TYPE;
		case "java.lang.Boolean":
			return Boolean.TYPE;
		case "java.lang.Short":
			return Short.TYPE;
		case "java.lang.Byte":
			return Byte.TYPE;
		}
		return classType;
	}
}
