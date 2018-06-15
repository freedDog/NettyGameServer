package com.game.service.net.tcp;

import com.game.common.constant.GlobalConstants;
import com.game.service.message.decoder.RpcDecoder;
import com.game.service.message.encoder.RpcEncoder;
import com.game.service.net.tcp.handler.GameNetRPCServerHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月4日 下午12:32:13
 */
public class GameNetRPCChannleInitializer extends ChannelInitializer<NioSocketChannel>{

	@Override
	protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
		ChannelPipeline channelPipeline=nioSocketChannel.pipeline();
		int maxLength=Integer.MAX_VALUE;
		channelPipeline.addLast("frame",new LengthFieldBasedFrameDecoder(maxLength, 0, 4,0,0));
		channelPipeline.addLast("decoder",new RpcDecoder(RpcRequest.class));
		channelPipeline.addLast("encoder",new RpcEncoder(RpcEncoder.class));
		int readerIdleTimeSeconds=0;
		int writerIdleTimeSeconds=0;
		int allIdleTimeSeconds=GlobalConstants.Net.SESSION_HEART_ALL_TIMEOUT;
		channelPipeline.addLast("idleStateHandler",new IdleStateHandler(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds));
		channelPipeline.addLast("logger",new LoggingHandler(LogLevel.DEBUG));
		channelPipeline.addLast("handler",new GameNetRPCServerHandler());
	}

}
