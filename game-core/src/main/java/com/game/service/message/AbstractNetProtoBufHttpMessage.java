package com.game.service.message;
/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月4日 下午12:36:34
 */
public abstract class AbstractNetProtoBufHttpMessage extends AbstractNetProtoBufMessage{

	public AbstractNetProtoBufHttpMessage() {
		super();
		setNetMessageHead(new NetHttpMessageHead());
		setNetMessageBody(new NetProtoBufMessageBody());
		initHeadCmd();
	}
}
