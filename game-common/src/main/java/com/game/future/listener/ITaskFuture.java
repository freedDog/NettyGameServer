package com.game.future.listener;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月27日 上午10:28:08
 */
public interface ITaskFuture<V> extends Future<V> {

	/**
	 * 是否成功
	 * @return
	 */
	public boolean isSuccess();
	
	/**
	 * 获取执行结果
	 * @return
	 */
	public V getNow();
	
	/**
	 * 是否可以取消
	 * @return
	 */
	public boolean isCancellable();
	
	/**
	 * 等待future 完成
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public ITaskFuture<V> await() throws InterruptedException,ExecutionException;
	
	/**
	 * 等待future 超时完成
	 * @param timeOutMills
	 * @return
	 * @throws InterruptedException
	 */
	public boolean await(long timeOutMills) throws InterruptedException;
	
	/**
	 * 等待future 超时完成
	 * @param timeOut
	 * @param timeUnit
	 * @return
	 * @throws InterruptedException
	 */
	public boolean await(long timeOut,TimeUnit timeUnit) throws InterruptedException;
	
	/**
	 * 等待future 完成，不响应中断
	 * @return
	 * @throws Exception
	 */
	public ITaskFuture<V> awaitUninterruptibly() throws Exception;
	
	/**
	 * 等待future超时完成 不响应中断
	 * @return
	 * @throws Exception
	 */
	public boolean awaitUninterruptibly(long timeOutMills);
	
	/**
	 * 等待future超时完成，不响应中断
	 * @param timeOut
	 * @param timeUnit
	 * @return
	 */
	public boolean awaitUninterruptibly(long timeOut,TimeUnit timeUnit);
	
	/**
	 * 添加监听器
	 * @param listener
	 * @return
	 */
	public ITaskFuture<V> addListener(ITaskFutureListener listener);
	
	/**
	 * 移除监听器
	 * @param listener
	 * @return
	 */
	public ITaskFuture<V> removeListener(ITaskFuture<V> listener);
	
}
