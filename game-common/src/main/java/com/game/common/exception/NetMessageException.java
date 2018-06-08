package com.game.common.exception;
/**
 * 网络消息发送异常
 * @author JiangBangMing
 *
 * 2018年5月31日 下午9:36:33
 */
public class NetMessageException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public NetMessageException(String name) {
		super(name);
	}
	public NetMessageException(String name,Throwable t) {
		super(name,t);
	}
	public NetMessageException(Throwable t) {
		super(t);
	}
	
}
