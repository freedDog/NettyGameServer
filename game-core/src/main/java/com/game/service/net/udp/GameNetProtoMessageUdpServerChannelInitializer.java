package com.game.service.net.udp;


import com.game.service.message.decoder.NetProtoBufMessageUDPDecoder;
import com.game.service.message.encoder.NetProtoBufMessageUDPEncoder;
import com.game.service.net.udp.handler.GameNetMessageUdpServerHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月14日 上午11:46:42
 */
public class GameNetProtoMessageUdpServerChannelInitializer extends ChannelInitializer<NioDatagramChannel>{

	@Override
	protected void initChannel(NioDatagramChannel ch) throws Exception {
		ChannelPipeline channelPipeline=ch.pipeline();
		int maxLength=Integer.MAX_VALUE;
		channelPipeline.addLast(new LengthFieldBasedFrameDecoder(maxLength, 2, 4,0,0));
		channelPipeline.addLast(new NetProtoBufMessageUDPEncoder());
		channelPipeline.addLast(new NetProtoBufMessageUDPDecoder());
		channelPipeline.addLast("logger",new LoggingHandler(LogLevel.DEBUG));
		channelPipeline.addLast(new GameNetMessageUdpServerHandler());
	}

}
