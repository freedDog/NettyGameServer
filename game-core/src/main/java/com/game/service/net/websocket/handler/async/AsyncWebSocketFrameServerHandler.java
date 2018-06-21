package com.game.service.net.websocket.handler.async;

import org.slf4j.Logger;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.constant.Loggers;
import com.game.common.exception.CodecException;
import com.game.common.exception.GameHandlerException;
import com.game.common.exception.NetMessageException;
import com.game.executor.event.EventParam;
import com.game.logic.net.NetMessageProcessLogic;
import com.game.service.event.GameAsyncEventService;
import com.game.service.event.impl.SessionRegisterEvent;
import com.game.service.event.impl.SessionUnRegisterEvent;
import com.game.service.lookup.NetTcpSessionLoopUpService;
import com.game.service.message.AbstractNetMessage;
import com.game.service.message.AbstractNetProtoBufMessage;
import com.game.service.message.decoder.NetProtoBufTcpMessageDecoderFactory;
import com.game.service.message.factory.TcpMessageFactory;
import com.game.service.net.tcp.MessageAttributeEnum;
import com.game.service.net.tcp.session.NettyTcpSession;
import com.game.service.net.tcp.session.builder.NettyTcpSessionBuilder;
import com.game.service.net.websocket.handler.WebSocketServerHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月20日 上午10:37:05
 */
public class AsyncWebSocketFrameServerHandler extends SimpleChannelInboundHandler<WebSocketFrame>{
	
	private static Logger logger=Loggers.handerLogger;
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
		handleWebSocketFrame(ctx, msg);
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
			ctx.close();
			return;
		}
		//生成aysnc事件
		long sessionId=nettyTcpSession.getSessionId();
		EventParam<NettyTcpSession> sessionEventParam=new EventParam<NettyTcpSession>(nettyTcpSession);
		SessionRegisterEvent sessionRegisterEvent=new SessionRegisterEvent(sessionId, sessionId, sessionEventParam);
		GameAsyncEventService gameAsyncEventService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameAsyncEventService();
		gameAsyncEventService.putEvent(sessionRegisterEvent);
	}


	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		NetTcpSessionLoopUpService netTcpSessionLoopUpService=LocalMananger.getInstance().getLocalSpringServiceManager().getNetTcpSessionLoopUpService();
		long sessionId=ctx.channel().attr(NettyTcpSessionBuilder.channel_session_id).get();
		NettyTcpSession nettyTcpSession=(NettyTcpSession)netTcpSessionLoopUpService.lookup(sessionId);
		disconnect(ctx.channel());
		
		if(null==nettyTcpSession) {
			ctx.fireChannelUnregistered();
			return;
		}
		
		netTcpSessionLoopUpService.removenettySession(nettyTcpSession);
		//因为updateService会自己删除，这里不需要逻辑
		
		//生成aysnc事件
		sessionId=nettyTcpSession.getSessionId();
		EventParam<NettyTcpSession> eventParam=new EventParam<NettyTcpSession>(nettyTcpSession);
		SessionUnRegisterEvent sessionUnRegisterEvent=new SessionUnRegisterEvent(sessionId, sessionId, eventParam);
		GameAsyncEventService gameAsyncEventService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameAsyncEventService();
		gameAsyncEventService.putEvent(sessionUnRegisterEvent);
		
		ctx.fireChannelUnregistered();
	}


	private void handleWebSocketFrame(ChannelHandlerContext ctx,WebSocketFrame frame) {
		//check for closing frame
		if(frame instanceof CloseWebSocketFrame) {
			WebSocketServerHandler webSocketServerHandler=(WebSocketServerHandler)ctx.pipeline().get("webSocketServerHandler");
			WebSocketServerHandshaker handshaker=webSocketServerHandler.getHandshaker();
			handshaker.close(ctx.channel(), (CloseWebSocketFrame)frame.retain());
			return;
		}
		if(frame instanceof PingWebSocketFrame) {
			ctx.write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		if(frame instanceof TextWebSocketFrame) {
			// Echo the frame
			ctx.write(frame.retain());
			return;
		}
		
		if(frame instanceof BinaryWebSocketFrame) {
			BinaryWebSocketFrame binaryWebSocketFrame=(BinaryWebSocketFrame)frame;
			ByteBuf buf=binaryWebSocketFrame.content();
			//开始解析
			NetProtoBufTcpMessageDecoderFactory netProtoBufTcpMessageDecoderFactory=LocalMananger.getInstance().getLocalSpringBeanManager().getNetProtoBufTcpMessageDecoderFactory();
			AbstractNetProtoBufMessage netMessage=null;
			try {
				netMessage=netProtoBufTcpMessageDecoderFactory.praseMessage(buf);
			}catch (CodecException e) {
				e.printStackTrace();
			}
			
			binaryWebSocketFrame.release();
			
			Channel channel=ctx.channel();
			//装配session
			NetTcpSessionLoopUpService netTcpSessionLoopUpService=LocalMananger.getInstance().getLocalSpringServiceManager().getNetTcpSessionLoopUpService();
			long sessionId=channel.attr(NettyTcpSessionBuilder.channel_session_id).get();
			NettyTcpSession nettyTcpSession=(NettyTcpSession)netTcpSessionLoopUpService.lookup(sessionId);
			if(null==nettyTcpSession) {
				logger.error("tcp netsession null channelId is :"+channel.id().asLongText());
				//已经丢失session ,停止处理
				return;
			}
			
			//封装属性
			netMessage.setAttribute(MessageAttributeEnum.DISPATCH_SESSION, nettyTcpSession);
			//进行处理
			NetMessageProcessLogic netMessageProcessLogic=LocalMananger.getInstance().getLocalSpringBeanManager().getNetMessageProcessLogic();
			netMessageProcessLogic.processWebSocketMessage(netMessage, ctx.channel());
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
