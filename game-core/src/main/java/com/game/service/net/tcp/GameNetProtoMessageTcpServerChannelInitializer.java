package com.game.service.net.tcp;


import com.game.bootstrap.manager.LocalMananger;
import com.game.common.config.GameServerConfig;
import com.game.common.constant.GlobalConstants;
import com.game.service.config.GameServerConfigService;
import com.game.service.message.decoder.NetProtoBufMessageTCPDecoder;
import com.game.service.message.encoder.NetProtoBufMessageTCPEncoder;
import com.game.service.net.tcp.handler.GameLoggingHandler;
import com.game.service.net.tcp.handler.GameNetMessageTcpServerHandler;
import com.game.service.net.tcp.handler.async.AsyncNettyGameNetMessageTcpServerHandler;
import com.game.service.net.tcp.handler.async.AsyncNettyTcpHandlerService;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 *  *  * LengthFieldBasedFrameDecoder 使用
 *  head+messagelength+serial+body
 *
 *  参数maxFrameLength 为数据帧最大长度
 *  参数lengthFieldOffset为version长度表示 从第几个字段开始读取长度，表示同意为head的长度
 *  参数lengthFieldLength表示占用了多少个字节数 具体可查看LengthFieldBasedFrameDecoder的getUnadjustedFrameLength方法
 *  参数lengthAdjustment表示还需要拓展长度，具体表示为serial的长度
 *  参数initialBytesToStrip表示 传递给下个coder的时候跳过多少字节 如果从0开始为 head+messagelength+serial+body全部给下个coder
 * @author JiangBangMing
 *
 * 2018年6月14日 下午3:49:51
 */
public class GameNetProtoMessageTcpServerChannelInitializer extends ChannelInitializer<NioSocketChannel>{

	@Override
	protected void initChannel(NioSocketChannel ch) throws Exception {
		ChannelPipeline channelPipeline=ch.pipeline();
		int maxLength=Integer.MAX_VALUE;
		channelPipeline.addLast("frame",new LengthFieldBasedFrameDecoder(maxLength, 2, 4,0,0));
		channelPipeline.addLast("encoder",new NetProtoBufMessageTCPEncoder());
		channelPipeline.addLast("decoder",new NetProtoBufMessageTCPDecoder());
		
		int readerIdleTimeSeconds=GlobalConstants.Net.SESSION_HEART_ALL_TIMEOUT;
		int writerIdleTimeSeconds=GlobalConstants.Net.SESSION_HEART_ALL_TIMEOUT;
		int allIdleTimeSeconds=GlobalConstants.Net.SESSION_HEART_ALL_TIMEOUT;
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		GameServerConfig gameServerConfig=gameServerConfigService.getGameServerConfig();
		if(gameServerConfig.isDevelopModel()) {
			channelPipeline.addLast("logger",new GameLoggingHandler(LogLevel.DEBUG));
		}
		channelPipeline.addLast("idleStateHandler",new IdleStateHandler(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds));
		
		boolean tcpMessageQueueDirectDispatch=gameServerConfig.isTcpMessageQueueDirectDispatch();
		if(tcpMessageQueueDirectDispatch) {
			channelPipeline.addLast("handler",new GameNetMessageTcpServerHandler());
		}else {
			AsyncNettyTcpHandlerService asyncNettyTcpHandlerService=LocalMananger.getInstance().getLocalSpringServiceManager().getAsyncNettyTcpHandlerService();
			channelPipeline.addLast(asyncNettyTcpHandlerService.getDefaultEventExecutorGroup(),new AsyncNettyGameNetMessageTcpServerHandler());
		}
		
	}

}
