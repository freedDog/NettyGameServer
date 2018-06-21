package com.game.service.net.proxy;

import org.jdom2.DataConversionException;
import org.jdom2.Element;
import com.game.service.net.SdNetConfig;

/**
 * 代理配置
 * @author JiangBangMing
 *
 * 2018年6月19日 下午7:36:26
 */
public class SdProxyConfig extends SdNetConfig{
	
	private String transferIp;
	private int transferPort;
	private int bossThreadSize;
	private int workerThreadSize;
	
	public void load(Element element) throws DataConversionException{
		super.load(element);
		transferIp=element.getChildTextTrim("transfer-ip");
		transferPort=Integer.parseInt(element.getChildTextTrim("transfer-port"));
	}
	

	public String getTransferIp() {
		return transferIp;
	}


	public void setTransferIp(String transferIp) {
		this.transferIp = transferIp;
	}

	public int getTransferPort() {
		return transferPort;
	}
	public void setTransferPort(int transferPort) {
		this.transferPort = transferPort;
	}
	public int getBossThreadSize() {
		return bossThreadSize;
	}
	public void setBossThreadSize(int bossThreadSize) {
		this.bossThreadSize = bossThreadSize;
	}
	public int getWorkerThreadSize() {
		return workerThreadSize;
	}
	public void setWorkerThreadSize(int workerThreadSize) {
		this.workerThreadSize = workerThreadSize;
	}
	
	
}
