package com.game.service.rpc.client.net;

import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.constant.Loggers;
import com.game.service.net.tcp.RpcRequest;
import com.game.service.net.tcp.RpcResponse;
import com.game.service.rpc.client.RPCFuture;
import com.game.service.rpc.client.RPCFutureService;
import com.game.service.rpc.server.RpcNodeInfo;

import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 检查客户端连接
 * @author JiangBangMing
 *
 * 2018年6月12日 下午9:24:05
 */
public class RpcClient {
	
	private  Logger logger=Loggers.rpcLogger;
	private RpcClientConnection rpcClientConnection;
	
	public RpcClient(RpcNodeInfo rpcNodeInfo,ExecutorService threadPool) {
		rpcClientConnection=new RpcClientConnection(this, rpcNodeInfo, threadPool);
	}
	
	public RPCFuture sendRequest(RpcRequest request) {
		RPCFuture rpcFuture=new RPCFuture(request);
		RPCFutureService rpcFutureService=LocalMananger.getInstance().getLocalSpringServiceManager().getRpcFutureService();
		rpcFutureService.addRPCFuture(request.getRequestId(), rpcFuture);
		rpcClientConnection.writeRequest(request);
		return rpcFuture;
	}
	
	public NioSocketChannel getChannel() {
		return rpcClientConnection.getChannel();
	}
	
	public void close() {
		logger.info("rpc client close");
		if(rpcClientConnection!=null) {
			rpcClientConnection.close();
		}
	}

	
	public void handleRpcResponser(RpcResponse rpcResponse) {
		String requestId=rpcResponse.getRequestId();
		RPCFutureService rpcFutureService=LocalMananger.getInstance().getLocalSpringServiceManager().getRpcFutureService();
		RPCFuture rpcFuture=rpcFutureService.getRPCFuture(requestId);
		if(rpcFuture!=null) {
			boolean removeFlag=rpcFutureService.removeRPCFuture(requestId, rpcFuture);
			if(removeFlag) {
				rpcFuture.done(rpcResponse);
			}else {
				//表示服务器已经处理过了，可能已经超时了
				logger.error("rpcFuture is remove "+requestId);
			}
		}
	}
	

	public RpcClientConnection getRpcClientConnection() {
		return rpcClientConnection;
	}
	
}
