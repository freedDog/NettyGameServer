package com.game.service.net.tcp.handler;

import org.slf4j.Logger;
import com.game.bootstrap.manager.LocalMananger;
import com.game.common.config.GameServerConfig;
import com.game.common.constant.Loggers;
import com.game.common.exception.GameHandlerException;
import com.game.common.exception.NetMessageException;
import com.game.executor.event.EventParam;
import com.game.service.config.GameServerConfigService;
import com.game.service.event.GameAsyncEventService;
import com.game.service.event.impl.SessionRegisterEvent;
import com.game.service.lookup.NetTcpSessionLoopUpService;
import com.game.service.message.AbstractNetMessage;
import com.game.service.message.factory.TcpMessageFactory;
import com.game.service.net.tcp.session.NettyTcpSession;
import com.game.service.net.tcp.session.builder.NettyTcpSessionBuilder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

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
		NetTcpSessionLoopUpService netTcpSessionLoopUpService=LocalMananger.getInstance().getLocalSpringServiceManager().getNetTcpSessionLoopUpService();
		boolean flag=netTcpSessionLoopUpService.addNettySession(nettyTcpSession);
		if(!flag) {
			//被限制不能加入
			TcpMessageFactory messageFactory=LocalMananger.getInstance().getLocalSpringBeanManager().getTcpMessageFactory();
			AbstractNetMessage abstractNetMessage=messageFactory.createCommonErrorResponseMessage(-1, GameHandlerException.COMMON_ERROR_MAX_CONNECT_TCP_SESSION_NUMBER);
			nettyTcpSession.write(abstractNetMessage);
			nettyTcpSession.close();
			return;
		}
		addUpdateSession(nettyTcpSession);
		//生成aysnc事件
		long sessionId=nettyTcpSession.getSessionId();
		EventParam<NettyTcpSession> sessionEventParam=new EventParam<NettyTcpSession>(nettyTcpSession);
		SessionRegisterEvent sessionRegisterEvent=new SessionRegisterEvent(sessionId, sessionId, sessionEventParam);
		GameAsyncEventService gameAsyncEventService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameAsyncEventService();
		gameAsyncEventService.putEvent(sessionRegisterEvent);
		
	}

	public abstract void addUpdateSession(NettyTcpSession nettyTcpSession);
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelUnregistered(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if(cause instanceof java.io.IOException) {
			return;
		}
		if(logger.isErrorEnabled()) {
			logger.error("channel exceptionCaught ",cause);
		}
		
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		GameServerConfig gameServerConfig=gameServerConfigService.getGameServerConfig();
		boolean exceptionCloseSessionFlag=gameServerConfig.isExceptionCloseSessionFlag();
		
		if(exceptionCloseSessionFlag) {
			//设置下线
			disconnect(ctx.channel());
			//销毁上下文
			ctx.close();
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		super.userEventTriggered(ctx, evt);
		if(evt instanceof IdleStateEvent) {
			//说是空闲事件
			disconnect(ctx.channel());
		}
	}
	
	private void disconnect(Channel channel) throws NetMessageException{
		NetTcpSessionLoopUpService netTcpSessionLoopUpService=LocalMananger.getInstance().getLocalSpringServiceManager().getNetTcpSessionLoopUpService();
		long sessionId=channel.attr(NettyTcpSessionBuilder.channel_session_id).get();
		NettyTcpSession nettyTcpSession=(NettyTcpSession)netTcpSessionLoopUpService.lookup(sessionId);
		if(null==nettyTcpSession) {
			logger.error("tcp netsession null channelId is:"+channel.id().asLongText());
			return;
		}
		nettyTcpSession.close();
	}
	
}
