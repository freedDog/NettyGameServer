package com.game.service.message;
/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月4日 下午12:39:17
 */

import com.google.protobuf.AbstractMessage;

public class NetProtoBufMessageBody extends NetMessageBody{

	/**
	 * 将字节读取为protobuf的抽象对象
	 */
	private AbstractMessage abstractMessage;

	public AbstractMessage getAbstractMessage() {
		return abstractMessage;
	}

	public void setAbstractMessage(AbstractMessage abstractMessage) {
		this.abstractMessage = abstractMessage;
	}
	
}
