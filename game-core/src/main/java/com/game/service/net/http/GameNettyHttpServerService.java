package com.game.service.net.http;

import io.netty.channel.ChannelInitializer;

/**
 * http 服务
 * @author JiangBangMing
 *
 * 2018年6月19日 下午6:44:16
 */
public class GameNettyHttpServerService extends AbstractNettyHttpServerService{

	public GameNettyHttpServerService(String serviceId, int serverPort, String bossThreadName, String workThreadName,
			ChannelInitializer channelInitializer) {
		super(serviceId, serverPort, bossThreadName, workThreadName, channelInitializer);
	}

}
