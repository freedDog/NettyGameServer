package com.game.service.message.process;

import com.game.service.message.AbstractNetMessage;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月1日 上午11:27:57
 */
public interface INetProtoBufMessageProcess {
	public void processNetMessage();
	public void addnetMessage(AbstractNetMessage abstractNetMessage);
	public void close();
}
