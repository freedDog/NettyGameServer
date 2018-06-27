package com.game.threadpool.thread.worker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 任务队列
 * @author JiangBangMing
 *
 * 2018年6月27日 下午12:43:00
 */
public class TasksQueue<V> {
	
	/**
	 * 命令队列
	 */
	private final BlockingQueue<V> taskQueue=new LinkedBlockingQueue<>();
	
	/**
	 * 是否处理完成
	 */
	private boolean processingCompleted=true;
	
	/**
	 * 下一执行命令
	 * @return
	 */
	public V poll() {
		return taskQueue.poll();
	}
	
	/**
	 * 增加执行命令
	 * @param value
	 * @return
	 */
	public boolean add(V value) {
		return taskQueue.add(value);
	}
	
	/**
	 * 清理
	 */
	public void clear() {
		taskQueue.clear();
	}
	
	/**
	 * 获取指令数量
	 * @return
	 */
	public int size() {
		return taskQueue.size();
	}

	public boolean isProcessingCompleted() {
		return processingCompleted;
	}

	public void setProcessingCompleted(boolean processingCompleted) {
		this.processingCompleted = processingCompleted;
	}
	
	
	
	
}
