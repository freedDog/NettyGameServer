package com.game.common.exception;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月14日 下午3:19:27
 */
public class StartUpException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public StartUpException(String name) {
		super(name);
	}
	
	public StartUpException(String name,Throwable t) {
		super(name, t);
	}
}
