package com.game.service.net.websocket;

import io.netty.channel.ChannelInitializer;

/**
 * websocket服务
 * @author JiangBangMing
 *
 * 2018年6月19日 下午9:02:33
 */
public class GameNettyWebSocketServerService extends AbstractNettyWebSocketServerService{

	public GameNettyWebSocketServerService(String serviceId, int serverPort, String bossThreadName,
			String workerThreadName, ChannelInitializer channelInitializer) {
		super(serviceId, serverPort, bossThreadName, workerThreadName, channelInitializer);
	}


}
