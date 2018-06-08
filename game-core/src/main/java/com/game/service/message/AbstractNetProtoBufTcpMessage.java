package com.game.service.message;
/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月4日 下午12:45:39
 */
public abstract class AbstractNetProtoBufTcpMessage extends AbstractNetProtoBufMessage{

	public AbstractNetProtoBufTcpMessage(){
		super();
		setNetMessageBody(new NetProtoBufMessageBody());
		initHeadCmd();
	}
}
