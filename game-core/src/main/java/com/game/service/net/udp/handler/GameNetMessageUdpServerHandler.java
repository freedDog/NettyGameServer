package com.game.service.net.udp.handler;

import com.game.bootstrap.manager.LocalMananger;
import com.game.service.message.AbstractNetProtoBufUdpMessage;
import com.game.service.net.tcp.pipeline.IServerPipeLine;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * udp 协议处理handler
 * @author JiangBangMing
 *
 * 2018年6月14日 上午11:03:21
 */
public class GameNetMessageUdpServerHandler extends SimpleChannelInboundHandler<AbstractNetProtoBufUdpMessage>{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, AbstractNetProtoBufUdpMessage msg) throws Exception {
		//获取管道
		IServerPipeLine iServerPipeLine=LocalMananger.getInstance().getLocalSpringBeanManager().getDefaultUdpServerPipeLine();
		iServerPipeLine.dispatchAction(ctx.channel(), msg);
	}

}
