package com.game.service.net.tcp.process;

import com.game.service.message.AbstractNetMessage;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月1日 下午4:15:41
 */
public interface IMessageProcessor {
	/**
	 * 启动消息处理器
	 */
	public void start();
	/**
	 * 停止消息处理器
	 */
	public void stop();
	/**
	 * 向消息队列投递消息
	 * @param msg
	 */
	public void put(AbstractNetMessage msg);
	/**
	 * 判断队列是否已经达到上限了
	 * @return
	 */
	public boolean isFull();
}
