package com.game.service.net.tcp.handler;

import org.slf4j.Logger;
import org.springframework.cglib.core.Local;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.constant.Loggers;
import com.game.service.net.tcp.session.NettyTcpSession;
import com.game.service.net.tcp.session.builder.NettyTcpSessionBuilder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月4日 上午10:44:30
 */
public abstract class AbstractGameNetMessageTcpServerHandler extends ChannelInboundHandlerAdapter{

	public static Logger logger=Loggers.handerLogger;

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		ctx.fireChannelRegistered();
		NettyTcpSessionBuilder nettyTcpSessionBuilder=LocalMananger.getInstance().getLocalSpringBeanManager().getNettyTcpSessionBuilder();
		NettyTcpSession nettyTcpSession=(NettyTcpSession)nettyTcpSessionBuilder.buildSession(ctx.channel());
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelUnregistered(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// TODO Auto-generated method stub
		super.userEventTriggered(ctx, evt);
	}
	
	
}
