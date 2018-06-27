package com.game.future.listener;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月27日 上午10:37:19
 */
public interface ITaskFutureListener<V extends ITaskFuture<?>> extends IEventListener{

	/**
	 * 完成
	 * @param future
	 * @throws Exception
	 */
	public void operationComplete(ITaskFuture<V> future) throws Exception;
}
