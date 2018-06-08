package com.game.service.net.tcp.process;

import com.game.service.message.AbstractNetMessage;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月1日 下午4:17:55
 */
public interface ITcpMessageProcessor extends IMessageProcessor{
	/**
	 * 向消息队列投递消息
	 * 直接投递到对象processor 上面
	 * @param msg
	 */
	public void directPutTcpMessage(AbstractNetMessage msg);
}
