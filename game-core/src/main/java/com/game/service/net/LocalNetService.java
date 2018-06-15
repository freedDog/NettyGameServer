package com.game.service.net;

import org.slf4j.Logger;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.config.GameServerConfig;
import com.game.common.constant.GlobalConstants;
import com.game.common.constant.Loggers;
import com.game.common.constant.ServiceName;
import com.game.common.exception.StartUpException;
import com.game.service.IService;
import com.game.service.config.GameServerConfigService;
import com.game.service.net.tcp.GameNetProtoMessageTcpServerChannelInitializer;
import com.game.service.net.tcp.GameNetRPCChannleInitializer;
import com.game.service.net.tcp.GameNettyRPCService;
import com.game.service.net.tcp.GameNettyTcpServerService;
import com.game.service.net.udp.GameNetProtoMessageUdpServerChannelInitializer;
import com.game.service.net.udp.GameNettyUdpServerService;
import com.game.service.net.udp.NetUdpServerConfig;
import com.game.service.net.udp.SdUdpServerConfig;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 本地网络服务
 * @author JiangBangMing
 *
 * 2018年6月4日 下午12:00:34
 */
public class LocalNetService implements IService{
	
	private Logger logger=Loggers.serverLogger;
	
	/**
	 * tcp服务
	 */
	private GameNettyTcpServerService gameNettyTcpServerService;
	/**
	 * udp服务
	 */
	private GameNettyUdpServerService gameNettyUdpServerService;
	/**
	 * rpc的tcp服务
	 */
	private GameNettyRPCService gameNettyRPCService;

	private ChannelInitializer<NioSocketChannel> nettyTcpChannelInitializer;
	private ChannelInitializer<NioDatagramChannel> nettyUdpChannelInitializer;
	private ChannelInitializer<NioSocketChannel> rpcChannelInitializer;
	private ChannelInitializer<NioSocketChannel> proxyChannelInitialier;
	private ChannelInitializer<SocketChannel> httpChannelInitialier;
	private ChannelInitializer<SocketChannel> webSocketChannelInitialier;
	
	public LocalNetService() {
	}
	
	@Override
	public String getId() {
		return ServiceName.LocalNetService;
	}

	@Override
	public void startup() throws Exception {
			initChannelInitializer();
			initNetSrvice();
	}

	@Override
	public void shutdown() throws Exception {
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		GameServerConfig gameServerConfig=gameServerConfigService.getGameServerConfig();
		if(gameNettyTcpServerService!=null) {
			gameNettyTcpServerService.stopService();
		}
		
		NetUdpServerConfig netUdpServerConfig=gameServerConfigService.getNetUdpServerConfig();
		if(netUdpServerConfig.getSdUdpServerConfig()!=null) {
			if(gameNettyUdpServerService!=null) {
				gameNettyUdpServerService.stopService();
			}
		}
		
		if(gameServerConfig.isRpcOpen()) {
			if(gameNettyRPCService!=null) {
				gameNettyRPCService.stopService();
			}
		}
	}

	public void initNetSrvice() throws Exception{
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		GameServerConfig gameServerConfig=gameServerConfigService.getGameServerConfig();
		gameNettyTcpServerService=new GameNettyTcpServerService(gameServerConfig.getServerId(), gameServerConfig.getPort(), 
				GlobalConstants.Thread.NET_TCP_BOSS,GlobalConstants.Thread.NET_TCP_WORKER, nettyTcpChannelInitializer);
		boolean startUpFlag=gameNettyTcpServerService.startService();
		if(!startUpFlag) {
			throw new StartUpException("tcp server startup error");
		}
		logger.info("gameNettyTcpServerService start "+startUpFlag);
		
		NetUdpServerConfig netUdpServerConfig=gameServerConfigService.getNetUdpServerConfig();
		SdUdpServerConfig sdUdpServerConfig=netUdpServerConfig.getSdUdpServerConfig();
		if(sdUdpServerConfig!=null) {
			gameNettyUdpServerService=new GameNettyUdpServerService(sdUdpServerConfig.getId(), sdUdpServerConfig.getPort(), 
					GlobalConstants.Thread.NET_UDP_WORKER, nettyUdpChannelInitializer);
			startUpFlag=gameNettyUdpServerService.startService();
			if(!startUpFlag) {
				throw new StartUpException("udp server startup error");
			}
			logger.info("gameNettyUdpSrverService start"+startUpFlag);
		}
		
		if(gameServerConfig.isRpcOpen()) {
			gameNettyRPCService=new GameNettyRPCService(gameServerConfig.getServerId(), gameServerConfig.getFirstRpcPort(), 
					GlobalConstants.Thread.NET_RPC_BOSS,GlobalConstants.Thread.NET_RPC_WORKER, rpcChannelInitializer);
			startUpFlag=gameNettyRPCService.startService();
			if(!startUpFlag) {
				throw new StartUpException("rpc server startup error");
			}
			
			logger.info("gameNettyRpcService start "+startUpFlag);
		}
	}
	public void initChannelInitializer() {
		nettyTcpChannelInitializer=new GameNetProtoMessageTcpServerChannelInitializer();
		nettyUdpChannelInitializer=new GameNetProtoMessageUdpServerChannelInitializer();
		rpcChannelInitializer=new GameNetRPCChannleInitializer();
		
	}

	public GameNettyTcpServerService getGameNettyTcpServerService() {
		return gameNettyTcpServerService;
	}

	public void setGameNettyTcpServerService(GameNettyTcpServerService gameNettyTcpServerService) {
		this.gameNettyTcpServerService = gameNettyTcpServerService;
	}

	public GameNettyUdpServerService getGameNettyUdpServerService() {
		return gameNettyUdpServerService;
	}

	public void setGameNettyUdpServerService(GameNettyUdpServerService gameNettyUdpServerService) {
		this.gameNettyUdpServerService = gameNettyUdpServerService;
	}

	public GameNettyRPCService getGameNettyRPCService() {
		return gameNettyRPCService;
	}

	public void setGameNettyRPCService(GameNettyRPCService gameNettyRPCService) {
		this.gameNettyRPCService = gameNettyRPCService;
	}

	public ChannelInitializer<NioSocketChannel> getNettyTcpChannelInitializer() {
		return nettyTcpChannelInitializer;
	}

	public void setNettyTcpChannelInitializer(ChannelInitializer<NioSocketChannel> nettyTcpChannelInitializer) {
		this.nettyTcpChannelInitializer = nettyTcpChannelInitializer;
	}

	public ChannelInitializer<NioDatagramChannel> getNettyUdpChannelInitializer() {
		return nettyUdpChannelInitializer;
	}

	public void setNettyUdpChannelInitializer(ChannelInitializer<NioDatagramChannel> nettyUdpChannelInitializer) {
		this.nettyUdpChannelInitializer = nettyUdpChannelInitializer;
	}
	
	
}
