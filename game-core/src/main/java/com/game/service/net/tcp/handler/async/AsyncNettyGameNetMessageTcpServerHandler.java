package com.game.service.net.tcp.handler.async;

import org.slf4j.Logger;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.constant.Loggers;
import com.game.logic.net.NetMessageProcessLogic;
import com.game.service.lookup.NetTcpSessionLoopUpService;
import com.game.service.message.AbstractNetProtoBufMessage;
import com.game.service.net.tcp.MessageAttributeEnum;
import com.game.service.net.tcp.handler.AbstractGameNetMessageTcpServerHandler;
import com.game.service.net.tcp.session.NettyTcpSession;
import com.game.service.net.tcp.session.builder.NettyTcpSessionBuilder;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * 使用AsyncNettyTcpHandlerService 的handler
 * 不会进行session 的游戏内循环经查，断网后直接删除缓存，抛出掉线事件
 * @author JiangBangMing
 *
 * 2018年6月13日 下午8:37:28
 */
public class AsyncNettyGameNetMessageTcpServerHandler extends AbstractGameNetMessageTcpServerHandler{

	public static Logger logger=Loggers.sessionLogger;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		AbstractNetProtoBufMessage netMessage=(AbstractNetProtoBufMessage)msg;
		Channel channel=ctx.channel();
		
		//直接进行处理
		//装配session
		NetTcpSessionLoopUpService netTcpSessionLoopUpService=LocalMananger.getInstance().getLocalSpringServiceManager().getNetTcpSessionLoopUpService();
		long sessionId=channel.attr(NettyTcpSessionBuilder.channel_session_id).get();
		NettyTcpSession nettyTcpSession=(NettyTcpSession)netTcpSessionLoopUpService.lookup(sessionId);
		if(null==nettyTcpSession) {
			logger.error("tcp netsession null channelId is:"+channel.id().asLongText());
			//已经丢失session ,停止处理
			return;
		}
		netMessage.setAttribute(MessageAttributeEnum.DISPATCH_SESSION, nettyTcpSession);
		//进行处理
		NetMessageProcessLogic netMessageProcessLogic=LocalMananger.getInstance().getLocalSpringBeanManager().getNetMessageProcessLogic();
		netMessageProcessLogic.processMessage(netMessage, nettyTcpSession);
	}

	@Override
	public void addUpdateSession(NettyTcpSession nettyTcpSession) {

		
	}
	
	
}
