package com.game.service.net.tcp.handler;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.constant.Loggers;
import com.game.service.net.tcp.RpcRequest;
import com.game.service.net.tcp.RpcResponse;
import com.game.service.rpc.server.RemoteRpcHandlerService;
import com.game.service.rpc.server.RpcMethodRegistry;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * rpc协议处理handler
 * @author JiangBangMing
 *
 * 2018年6月12日 下午1:44:01
 */
public class GameNetRPCServerHandler extends SimpleChannelInboundHandler<RpcRequest>{
	
	private Logger logger=Loggers.rpcLogger;

	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
		RemoteRpcHandlerService remoteRpcHandlerService=LocalMananger.getInstance().getLocalSpringServiceManager().getRemoteRpcHandlerService();
		remoteRpcHandlerService.submit(new Runnable() {
			
			@Override
			public void run() {
				if(logger.isDebugEnabled()) {
					logger.debug("Receive request "+request.getRequestId());
				}
				RpcResponse response=new RpcResponse();
				response.setRequestId(request.getRequestId());
				try {
					Object result=handle(request);
					response.setReesult(result);
				}catch (Throwable e) {
					response.setError(e.toString());
					logger.error("RPC Server handle request error ",e);
				}
				ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
					
					@Override
					public void operationComplete(ChannelFuture arg0) throws Exception {
						if(logger.isDebugEnabled()) {
							logger.debug("Send response for request "+request.getRequestId());
						}
					}
				});
			}
		});
	}


	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if(logger.isErrorEnabled()) {
			logger.error("server caught exception ",cause);
		}
		ctx.close();
	}
	
	private Object handle(RpcRequest request) throws Throwable{
		String className=request.getClassName();
		RpcMethodRegistry rpcMethodRegistry=LocalMananger.getInstance().getLocalSpringServiceManager().getRpcMethodRegistry();
		Object serviceBean=rpcMethodRegistry.getServiceBean(className);
		Class<?> serviceClass=serviceBean.getClass();
		String methodName=request.getMethodName();
		Class<?>[] parameterTypes=request.getParameterTypes();
		Object[] parameeters=request.getParameters();
		if(logger.isDebugEnabled()) {
			logger.debug(serviceClass.getName());
			logger.debug(methodName);
			for(int i=0;i<parameterTypes.length;i++) {
				logger.debug(parameterTypes[i].getName());
			}
			for(int i=0;i<parameeters.length;i++) {
				logger.debug(parameeters[i].toString());
			}
		}
		
		Method method=serviceClass.getMethod(methodName, parameterTypes);
		return method.invoke(serviceBean, parameeters);
	}

	
}
