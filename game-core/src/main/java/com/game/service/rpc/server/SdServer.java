package com.game.service.rpc.server;



import org.jdom2.DataConversionException;
import org.jdom2.Element;

/**
 * 服务器
 * @author JiangBangMing
 *
 * 2018年6月12日 下午6:29:44
 */
public class SdServer {

	/**
	 * 服务器配置id
	 */
	private int serverId;
	private String ip;
	private int port;
	/**
	 * 域名
	 */
	private String domain;
	/**
	 * 域名端口
	 */
	private int domainPort;
	/**
	 * 权重
	 */
	private int weight;
	/**
	 * 最大数量
	 */
	private int maxNumber;
	/**
	 * 通讯短端口
	 */
	private int rpcPort;
	/**
	 * 通讯链接数量
	 */
	private int rpcClientNumber;
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public int getDomainPort() {
		return domainPort;
	}
	public void setDomainPort(int domainPort) {
		this.domainPort = domainPort;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getMaxNumber() {
		return maxNumber;
	}
	public void setMaxNumber(int maxNumber) {
		this.maxNumber = maxNumber;
	}
	public int getRpcPort() {
		return rpcPort;
	}
	public void setRpcPort(int rpcPort) {
		this.rpcPort = rpcPort;
	}
	public int getRpcClientNumber() {
		return rpcClientNumber;
	}
	public void setRpcClientNumber(int rpcClientNumber) {
		this.rpcClientNumber = rpcClientNumber;
	}
	
	public void load(Element element) throws DataConversionException{
		this.serverId=element.getAttribute("serverId").getIntValue();
		this.domain=element.getAttributeValue("domain");
		this.domainPort=element.getAttribute("domainPort").getIntValue();
		this.ip=element.getAttributeValue("ip");
		this.port=element.getAttribute("port").getIntValue();
		this.weight=element.getAttribute("weight").getIntValue();
		this.maxNumber=element.getAttribute("maxNumber").getIntValue();
		this.rpcPort=element.getAttribute("rpcPort").getIntValue();
		this.rpcClientNumber=element.getAttribute("rpcClientNumber").getIntValue();
	}
	
}
