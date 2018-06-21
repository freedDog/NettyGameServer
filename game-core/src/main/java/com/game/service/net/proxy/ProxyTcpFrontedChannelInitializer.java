package com.game.service.net.proxy;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.config.GameServerConfig;
import com.game.common.constant.GlobalConstants;
import com.game.service.config.GameServerConfigService;
import com.game.service.message.decoder.NetProtoBufMessageTCPDecoder;
import com.game.service.message.encoder.NetProtoBufMessageTCPEncoder;
import com.game.service.net.proxy.handler.ProxyFrontendHandler;
import com.game.service.net.tcp.handler.GameLoggingHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 代理的网络初始化器
 * @author JiangBangMing
 *
 * 2018年6月19日 下午8:16:20
 */
public class ProxyTcpFrontedChannelInitializer extends ChannelInitializer<NioSocketChannel>{

	@Override
	protected void initChannel(NioSocketChannel ch) throws Exception {
		ChannelPipeline channelPipeline=ch.pipeline();
		int maxLength=Integer.MAX_VALUE;
		channelPipeline.addLast("frame", new LengthFieldBasedFrameDecoder(maxLength, 2, 4,0,0));
		channelPipeline.addLast("encoder",new NetProtoBufMessageTCPEncoder());
		channelPipeline.addLast("decoder",new NetProtoBufMessageTCPDecoder());
		
		int readIdleTimeSeconds=GlobalConstants.Net.SESSION_HEART_ALL_TIMEOUT;
		int writerIdleTimeSeconds=GlobalConstants.Net.SESSION_HEART_ALL_TIMEOUT;
		int allIdleTimeSeconds=GlobalConstants.Net.SESSION_HEART_ALL_TIMEOUT;
		
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		GameServerConfig gameServerConfig=gameServerConfigService.getGameServerConfig();
		if(gameServerConfig.isDevelopModel()) {
			channelPipeline.addLast("logger",new GameLoggingHandler(LogLevel.DEBUG));
		}
		channelPipeline.addLast("idleStateHandler",new IdleStateHandler(readIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds));
		channelPipeline.addLast("handler",new ProxyFrontendHandler());
	
	}

}
