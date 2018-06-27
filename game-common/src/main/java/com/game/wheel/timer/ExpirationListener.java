package com.game.wheel.timer;

/**
 * 监听器
 * @author JiangBangMing
 *
 * 2018年6月27日 下午2:07:37
 */
public interface ExpirationListener<E> {

	public void expired(E expireObject);
}
