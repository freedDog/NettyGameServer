package com.game.service.net.proxy;

import com.game.service.lookup.ILongId;

/**
 * 代理规则
 * @author JiangBangMing
 *
 * 2018年6月19日 下午7:33:23
 */
public class ProxyRule implements ILongId{
	
	private long serverId;
	/**
	 * 远程地址
	 */
	private String remoteHost;
	/**
	 * 远程端口
	 */
	private int remotePort;
	@Override
	public long longId() {
		return serverId;
	}
	public long getServerId() {
		return serverId;
	}
	public void setServerId(long serverId) {
		this.serverId = serverId;
	}
	public String getRemoteHost() {
		return remoteHost;
	}
	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}
	public int getRemotePort() {
		return remotePort;
	}
	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}

	
}
