package com.game.service.net.tcp.pipeline;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.config.GameServerConfig;
import com.game.common.constant.Loggers;
import com.game.service.config.GameServerConfigService;
import com.game.service.lookup.NetTcpSessionLoopUpService;
import com.game.service.message.AbstractNetMessage;
import com.game.service.message.AbstractNetProtoBufMessage;
import com.game.service.message.command.MessageCommand;
import com.game.service.message.registry.MessageRegistry;
import com.game.service.net.tcp.MessageAttributeEnum;
import com.game.service.net.tcp.process.GameTcpMessageProcessor;
import com.game.service.net.tcp.session.NettyTcpSession;
import com.game.service.net.tcp.session.builder.NettyTcpSessionBuilder;
import com.game.service.rpc.server.RpcServerRegisterConfig;

import io.netty.channel.Channel;

/**
 * 处理管道
 * @author JiangBangMing
 *
 * 2018年6月13日 下午9:48:09
 */
@Service
public class DefaultTcpServerPipeLine implements IServerPipeLine{

	public static Logger logger=Loggers.sessionLogger;
	@Override
	public void dispatchAction(Channel channel, AbstractNetMessage abstractNetMessage) {
		short commandId=abstractNetMessage.getNetMessageHead().getCmd();
		MessageRegistry messageRegistry=LocalMananger.getInstance().getLocalSpringServiceManager().getMessageRegistry();
		MessageCommand messageCommand=messageRegistry.getMessageCommand(commandId);
		AbstractNetProtoBufMessage abstractNetProtoBufMessage=(AbstractNetProtoBufMessage)abstractNetMessage;
		if(logger.isDebugEnabled()) {
			logger.debug("RECV_TCP_PROBUF_MESSGE commandId :"+messageCommand.getCommand_id()+" class:"+abstractNetMessage.getClass().getSimpleName());
		}
		NetTcpSessionLoopUpService netTcpSessionLoopUpService=LocalMananger.getInstance().getLocalSpringServiceManager().getNetTcpSessionLoopUpService();
		long sessionId=channel.attr(NettyTcpSessionBuilder.channel_session_id).get();
		NettyTcpSession nettyTcpSession=(NettyTcpSession)netTcpSessionLoopUpService.lookup(sessionId);
		if(null==nettyTcpSession) {
			logger.error("tcp netsession null channelId is:"+channel.id().asLongText());
		}
		
		//检查是否可以处理消息
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		//如果是通用消息，不进行服务器检测
		RpcServerRegisterConfig rpcServerRegisterConfig=gameServerConfigService.getRpcServerRegisterConfig();
		if(!rpcServerRegisterConfig.validServer(messageCommand.bo_id)) {
			if(logger.isDebugEnabled()) {
				logger.debug("discard tcp messsage sessionId:"+nettyTcpSession.getSessionId()+" PlayerId:"+nettyTcpSession.getPlayerId()+" messageId is "+commandId);
			}
			return;
		}
		
		GameServerConfig gameServerConfig=gameServerConfigService.getGameServerConfig();
		if(gameServerConfig.isDevelopModel()&&logger.isDebugEnabled()) {
			logger.debug("sessionId "+nettyTcpSession.getSessionId()+" playerId"+nettyTcpSession.getPlayerId()+" read tcp message "+commandId+" info"+abstractNetProtoBufMessage.toAllInfoString());
		}
		
		
		if(messageCommand.isIs_need_filter()) {
			int serial=abstractNetMessage.getSerial();
			long playerId=nettyTcpSession.getPlayerId();
		}
		
		//放入处理队列
		abstractNetMessage.setAttribute(MessageAttributeEnum.DISPATCH_SESSION, nettyTcpSession);
		GameTcpMessageProcessor gameTcpMessageProcessor=LocalMananger.getInstance().getGameTcpMessageProcessor();
		//取消判断直接分发
		gameTcpMessageProcessor.directPutTcpMessage(abstractNetMessage);
		
	}

}
