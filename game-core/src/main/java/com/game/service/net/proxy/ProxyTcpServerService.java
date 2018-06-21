package com.game.service.net.proxy;

import com.game.service.net.tcp.AbstractNettyTcpServerService;

import io.netty.channel.ChannelInitializer;

/**
 * 网络代理服务，用于支持网关
 * @author JiangBangMing
 *
 * 2018年6月19日 下午7:43:58
 */
public class ProxyTcpServerService extends AbstractNettyTcpServerService{

	public ProxyTcpServerService(String serviceId, int serverPort, String boosThreadName, String workerThreadName,
			ChannelInitializer channelInitializer) {
		super(serviceId, serverPort, boosThreadName, workerThreadName, channelInitializer);
	}

}
