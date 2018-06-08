package com.game.common.exception;
/**
 * 游戏处理错误
 * @author JiangBangMing
 *
 * 2018年6月1日 下午12:19:24
 */
public class GameHandlerException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public static final int COMMON_ERROR_STATE=6000;
	public static final int COMMON_ERROR_MAX_CONNECT_TCP_SESSION_NUMBER=6001;
	private int serial;
	
	public GameHandlerException(String name) {
		super(name);
	}
	
	public GameHandlerException(String name,Throwable t) {
		super(name,t);
	}
	
	public GameHandlerException(Exception e) {
		super(e);
	}
	
	public GameHandlerException(Exception e,int serial) {
		super(e);
		this.serial=serial;
	}
	
	public int getSerial() {
		return this.serial;
	}
}
