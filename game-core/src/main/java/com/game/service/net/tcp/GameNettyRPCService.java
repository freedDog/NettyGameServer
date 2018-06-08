package com.game.service.net.tcp;

import io.netty.channel.ChannelInitializer;

/**
 * 增加rpc 服务
 * @author JiangBangMing
 *
 * 2018年6月4日 下午12:30:55
 */
public class GameNettyRPCService extends AbstractNettyTcpServerService{

	public GameNettyRPCService(String serviceId, int serverPort, String boosThreadName, String workerThreadName,
			ChannelInitializer channelInitializer) {
		super(serviceId, serverPort, boosThreadName, workerThreadName, channelInitializer);
	}

	@Override
	public boolean startService() throws Exception {
		return super.startService();
	}

	@Override
	public boolean stopService() throws Exception {
		return super.stopService();
	}
	
	

}
