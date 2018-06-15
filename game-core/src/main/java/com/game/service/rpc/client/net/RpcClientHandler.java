package com.game.service.rpc.client.net;

import org.slf4j.Logger;

import com.game.common.constant.Loggers;
import com.game.service.net.tcp.RpcResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月12日 下午11:47:19
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse>{
	
	private Logger logger=Loggers.rpcLogger;
	//那个服务器
	private RpcClient rpcClient;
	@Override
	protected void channelRead0(ChannelHandlerContext arg0, RpcResponse response) throws Exception {
		rpcClient.handleRpcResponser(response);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("rpc client caught exception",cause);
		rpcClient.close();
	}

	public RpcClient getRpcClient() {
		return rpcClient;
	}
	public void setRpcClient(RpcClient rpcClient) {
		this.rpcClient = rpcClient;
	}
	
	

}
