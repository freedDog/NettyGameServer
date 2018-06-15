package com.game.service.net.udp;

import io.netty.channel.ChannelInitializer;

/**
 * udp 启动服务
 * @author JiangBangMing
 *
 * 2018年6月14日 上午11:51:29
 */
public class GameNettyUdpServerService extends AbstractNettyUdpServerService{

	public GameNettyUdpServerService(String serviceId, int serverPort, String threadNameFactoryName,
			ChannelInitializer channelInitializer) {
		super(serviceId, serverPort, threadNameFactoryName, channelInitializer);
	} 

}
