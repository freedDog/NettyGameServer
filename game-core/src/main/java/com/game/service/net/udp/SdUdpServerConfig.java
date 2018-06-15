package com.game.service.net.udp;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

import com.game.service.net.SdNetConfig;

/**
 * udp 服务器配置
 * @author JiangBangMing
 *
 * 2018年6月14日 上午10:49:26
 */
public class SdUdpServerConfig extends SdNetConfig{

	private int udpQueueMessageProcessWorkerSize;
	private boolean udpMessageOrderQueueFlag;
	
	public void load(Element element) throws DataConversionException{
		super.load(element);
		udpQueueMessageProcessWorkerSize=Integer.valueOf(element.getChildTextTrim("updQueueMessageProcessWorkerSize"));
		udpMessageOrderQueueFlag=Boolean.valueOf(element.getChildTextTrim("udpMessageOrderQueueFlag"));
	}
	
	public int getUdpQueueMessageProcessWorkerSize() {
		return udpQueueMessageProcessWorkerSize;
	}
	public void setUdpQueueMessageProcessWorkerSize(int udpQueueMessageProcessWorkerSize) {
		this.udpQueueMessageProcessWorkerSize = udpQueueMessageProcessWorkerSize;
	}
	public boolean isUdpMessageOrderQueueFlag() {
		return udpMessageOrderQueueFlag;
	}
	public void setUdpMessageOrderQueueFlag(boolean udpMessageOrderQueueFlag) {
		this.udpMessageOrderQueueFlag = udpMessageOrderQueueFlag;
	}
	
	
}
