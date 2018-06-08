package com.game.service.async;

/**
 * 异步线程调用
 * @author JiangBangMing
 *
 * 2018年6月6日 下午12:35:01
 */
public interface AsyncCall extends Runnable {

	public void call() throws Exception;
}
