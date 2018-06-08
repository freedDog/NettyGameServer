package com.game.executor.event;

/**
 * 事件参数
 * @author JiangBangMing
 *
 * 2018年6月6日 下午5:39:41
 */
public class EventParam<T> {
	
	private T t;
	
	public EventParam(T t) {
		this.t=t;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}
	
	

}
