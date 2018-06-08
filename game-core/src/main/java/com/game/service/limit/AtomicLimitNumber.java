package com.game.service.limit;
/**
 * 原子计数器
 * @author JiangBangMing
 *
 * 2018年6月4日 上午11:35:46
 */

import java.util.concurrent.atomic.AtomicLong;

public class AtomicLimitNumber {
	
	/**
	 * 原子数量
	 */
	private AtomicLong atomicLong;
	
	public AtomicLimitNumber() {
		this.atomicLong=new AtomicLong();
	}
	
	public long increment() {
		return this.atomicLong.incrementAndGet();
	}
	public long decrement() {
		return this.atomicLong.decrementAndGet();
	}
	
}
