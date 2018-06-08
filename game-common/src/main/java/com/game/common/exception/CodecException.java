package com.game.common.exception;
/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月1日 上午11:39:36
 */
public class CodecException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public CodecException(String name) {
		super(name);
	}
	public CodecException(String name,Throwable t) {
		super(name,t);
	}
}
