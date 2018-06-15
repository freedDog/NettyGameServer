package com.game.service.net.tcp.pipeline;

import org.slf4j.Logger;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.config.GameServerConfig;
import com.game.common.constant.DynamicPropertiesEnum;
import com.game.common.constant.Loggers;
import com.game.logic.player.GamePlayer;
import com.game.service.config.GameServerConfigService;
import com.game.service.lookup.GamePlayerLoopUpService;
import com.game.service.message.AbstractNetMessage;
import com.game.service.message.AbstractNetProtoBufMessage;
import com.game.service.message.AbstractNetProtoBufUdpMessage;
import com.game.service.message.command.MessageCommand;
import com.game.service.message.registry.MessageRegistry;
import com.game.service.net.tcp.MessageAttributeEnum;
import com.game.service.net.tcp.process.GameUdpMessageOrderProcessor;
import com.game.service.net.tcp.process.GameUdpMessageProcessor;
import com.game.service.net.udp.NetUdpServerConfig;
import com.game.service.net.udp.session.NettyUdpSession;
import com.game.service.rpc.server.RpcServerRegisterConfig;

import io.netty.channel.Channel;

/**
 * udp协议暂时假定不需要返回数据
 * @author JiangBangMing
 *
 * 2018年6月14日 上午10:33:22
 */

@Service
public class DefaultUdpServerPipeLine implements IServerPipeLine{

	public static Logger logger=Loggers.sessionLogger;
	
	@Override
	public void dispatchAction(Channel channel, AbstractNetMessage abstractNetMessage) {
		short commandId=abstractNetMessage.getNetMessageHead().getCmd();
		MessageRegistry messageRegistry=LocalMananger.getInstance().getLocalSpringServiceManager().getMessageRegistry();
		MessageCommand messageCommand=messageRegistry.getMessageCommand(commandId);
		AbstractNetProtoBufUdpMessage message=(AbstractNetProtoBufUdpMessage)abstractNetMessage;
		if(logger.isDebugEnabled()) {
			logger.debug("RECV_UDP_PROBUF_MESSAGE commandId : "+messageCommand.getCommand_id()+" class : "+abstractNetMessage.getClass().getSimpleName());
		}
		
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		//检查是否可以处理该消息
		GameServerConfig gameServerConfig=gameServerConfigService.getGameServerConfig();
		RpcServerRegisterConfig rpcServerRegisterConfig=gameServerConfigService.getRpcServerRegisterConfig();
		//如果是通用消息，不进行服务器检测
		if(!rpcServerRegisterConfig.validServer(messageCommand.bo_id)) {
			if(logger.isDebugEnabled()) {
				logger.debug("discard udp message playerId:"+message.getPlayerId()+" messageId is "+commandId);
			}
			return;
		}
		if(gameServerConfig.isDevelopModel()) {
			if(logger.isDebugEnabled()) {
				logger.debug(" playerId:"+message.getPlayerId()+" read message "+commandId+" info "+message.toAllInfoString());
			}
		}
		
		int serial=abstractNetMessage.getSerial();
		long playerId=message.getPlayerId();
		boolean checkFlag=gameServerConfigService.getGameDynamicPropertiesConfig().getProperty(DynamicPropertiesEnum.udp_message_tocken_check_flag.toString(), false);
		if(messageCommand.isIs_need_filter()&&checkFlag) {
			GamePlayerLoopUpService gamePlayerLoopUpService=LocalMananger.getInstance().getLocalSpringServiceManager().getGamePlayerLoopUpService();
			GamePlayer gamePlayer=gamePlayerLoopUpService.lookup(playerId);
			if(null==gamePlayer) {
				if(logger.isDebugEnabled()) {
					logger.debug("player not exsit discard udp message playerId:"+message.getPlayerId()+" messageId is "+commandId);
				}
				return;
			}
			//放入处理队列
			//TODO 优化UDPsession
			message.setAttribute(MessageAttributeEnum.DISPATCH_SESSION, new NettyUdpSession(channel));
			NetUdpServerConfig netUdpServerConfig=gameServerConfigService.getNetUdpServerConfig();
			if(netUdpServerConfig.getSdUdpServerConfig().isUdpMessageOrderQueueFlag()) {
				GameUdpMessageOrderProcessor gameUdpMessageOrderProcessor=LocalMananger.getInstance().getGameUdpMessageOrderProcessor();
				gameUdpMessageOrderProcessor.put(message);
			}else {
				GameUdpMessageProcessor gameUdpMessageProcessor=LocalMananger.getInstance().getGameUdpMessageProcessor();
				gameUdpMessageProcessor.put(message);
			}
		}
		
		
	}

}
