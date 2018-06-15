package com.game.service.rpc.server;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月12日 下午6:27:45
 */
public class RpcNodeInfo {

	/**
	 * 服务id
	 */
	private String serverId;
	private String host;
	private String port;
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	
	public int getIntPort() {
		return Integer.parseInt(this.port);
	}
	
}
