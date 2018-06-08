package com.game.service.message;
/**
 * 网络基本消息
 * @author JiangBangMing
 *
 * 2018年5月31日 下午8:45:47
 */

import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractNetMessage implements INetMessage{
	private NetMessageHead netMessageHead;
	private NetMessageBody netMessageBody;
	
	/**
	 * 增加默认属性(附带逻辑调用需要的属性)
	 */
	private final ConcurrentHashMap<Object, Object> attributes=new ConcurrentHashMap<Object, Object>(3);

	public NetMessageHead getNetMessageHead() {
		return netMessageHead;
	}

	public void setNetMessageHead(NetMessageHead netMessageHead) {
		this.netMessageHead = netMessageHead;
	}

	public NetMessageBody getNetMessageBody() {
		return netMessageBody;
	}

	public void setNetMessageBody(NetMessageBody netMessageBody) {
		this.netMessageBody = netMessageBody;
	}
	public int getSerial() {
		return this.netMessageHead.getSerial();
	}
	/**
	 * 逻辑处理时附带的参数
	 * @param key
	 * @param value
	 * @return
	 */
	public Object setAttribute(Object key,Object value) {
		return this.attributes.put(key, value);
	}
	public Object getAttribute(Object key) {
		return this.attributes.get(key);
	}
	public void removeAttribute(Object key) {
		this.attributes.remove(key);
	}
	public int getCmd() {
		return this.getNetMessageHead().getCmd();
	}
}
