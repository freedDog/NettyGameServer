package com.game.service.rpc.client.net;

import com.game.service.message.decoder.RpcDecoder;
import com.game.service.message.encoder.RpcEncoder;
import com.game.service.net.tcp.RpcRequest;
import com.game.service.net.tcp.RpcResponse;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月12日 下午11:43:32
 */
public class RpcClientInitializer extends ChannelInitializer<SocketChannel>{

	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		ChannelPipeline channelPipeline=socketChannel.pipeline();
		int maxLength=Integer.MAX_VALUE;
		channelPipeline.addLast(new LengthFieldBasedFrameDecoder(maxLength, 0, 4,0,0));
		channelPipeline.addLast(new RpcEncoder(RpcRequest.class));
		channelPipeline.addLast(new RpcDecoder(RpcResponse.class));
		channelPipeline.addLast("logger",new LoggingHandler(LogLevel.DEBUG));
		channelPipeline.addLast(new RpcClientHandler());
	}

}
