package com.game.service.net.http;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.config.GameServerConfig;
import com.game.service.config.GameServerConfigService;
import com.game.service.net.http.handler.HttpServerHandler;
import com.game.service.net.http.handler.async.AsyncNettyHttpHandlerService;
import com.game.service.net.tcp.handler.GameLoggingHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月19日 下午5:49:53
 */
public class GameNetProtoMessageHttpServerChannelInitializer extends ChannelInitializer<SocketChannel>{

	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		
		ChannelPipeline channelPipeline=socketChannel.pipeline();
		channelPipeline.addLast("encoder",new HttpResponseEncoder());
		channelPipeline.addLast("decoder",new HttpRequestDecoder());
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		GameServerConfig gameServerConfig=gameServerConfigService.getGameServerConfig();
		if(gameServerConfig.isDevelopModel()) {
			channelPipeline.addLast("logger",new GameLoggingHandler(LogLevel.DEBUG));
		}
		
		AsyncNettyHttpHandlerService asyncNettyHttpHandlerService=LocalMananger.getInstance().getLocalSpringServiceManager().getAsyncNettyHttpHandlerService();
		channelPipeline.addLast(asyncNettyHttpHandlerService.getDefaultEventExecutorGroup(),new HttpServerHandler());
	}

}
