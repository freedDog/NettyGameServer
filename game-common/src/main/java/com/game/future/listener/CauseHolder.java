package com.game.future.listener;

/**
 * 错误原因
 * @author JiangBangMing
 *
 * 2018年6月27日 上午10:41:43
 */
public class CauseHolder {

	public  Throwable cause;
	
	public CauseHolder(Throwable cause) {
		this.cause=cause;
	}
	
	public CauseHolder() {
		
	}
}
