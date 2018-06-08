package com.game.service.net.tcp;

import io.netty.channel.ChannelInitializer;

/**
 * 游戏里的tcp 服务
 * @author JiangBangMing
 *
 * 2018年6月4日 下午12:25:20
 */
public class GameNettyTcpServerService extends AbstractNettyTcpServerService{

	public GameNettyTcpServerService(String serviceId, int serverPort, String boosThreadName, String workerThreadName,
			ChannelInitializer channelInitializer) {
		super(serviceId, serverPort, boosThreadName, workerThreadName, channelInitializer);
	}

}
