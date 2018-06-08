package com.game.service.net.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

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
		channelPipeline.addLast("decoder",new Rpc)
	}

}
