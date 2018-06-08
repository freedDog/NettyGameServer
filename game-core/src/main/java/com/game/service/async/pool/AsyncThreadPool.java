package com.game.service.async.pool;

import java.util.concurrent.Future;

import com.game.service.async.AsyncCall;

/**
 * 异步线程池
 * @author JiangBangMing
 *
 * 2018年6月6日 下午12:36:17
 */
public interface AsyncThreadPool {

	public Future submit(AsyncCall asyncCall);
}
