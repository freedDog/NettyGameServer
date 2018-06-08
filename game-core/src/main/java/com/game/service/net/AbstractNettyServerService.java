package com.game.service.net;

import java.net.InetSocketAddress;

import com.game.service.net.tcp.AbstractServerService;
/**
 * 抽象的tcp服务
 * @author JiangBangMing
 *
 * 2018年6月4日 下午12:03:24
 */
public abstract class AbstractNettyServerService extends AbstractServerService{

	protected int serverPort;
	protected InetSocketAddress serverAddress;
	
	public AbstractNettyServerService(String serviceId,int serverPort) {
		super(serviceId);
		this.serverPort=serverPort;
		this.serverAddress=new InetSocketAddress(serverPort);
	}
}
