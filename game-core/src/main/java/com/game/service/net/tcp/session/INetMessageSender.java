package com.game.service.net.tcp.session;

import com.game.common.exception.NetMessageException;
import com.game.service.message.AbstractNetMessage;

/**
 * 消息处理器
 * @author JiangBangMing
 *
 * 2018年6月1日 上午10:57:03
 */
public interface INetMessageSender {
	/**
	 * 发送消息
	 * @param abstractNetMessage
	 * @return
	 * @throws NetMessageException
	 */
	public boolean sendMessage(AbstractNetMessage abstractNetMessage) throws NetMessageException;
	/**
	 * 关闭
	 * @throws NetMessageException
	 */
	public void close() throws NetMessageException;
}
