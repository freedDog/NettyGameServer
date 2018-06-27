package com.game.threadpool.thread.worker;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务队列集合
 * @author JiangBangMing
 *
 * 2018年6月27日 下午12:47:42
 */
public class OrderedQueuePool<K,V> {

	ConcurrentHashMap<K, TasksQueue<V>> map=new ConcurrentHashMap<>();
	
	/**
	 * 获取任务队列
	 * @param key
	 * @return
	 */
	public TasksQueue<V> getTaskQueue(K key){
		TasksQueue<V> queue=map.get(key);
		if(queue==null) {
			TasksQueue<V> newQueue=new TasksQueue<V>();
			queue=map.putIfAbsent(key, newQueue);
			if(queue==null) {
				queue=newQueue;
			}
		}
		return queue;
	}
	
	/**
	 * 获取全部任务队列
	 * @return
	 */
	public ConcurrentHashMap<K, TasksQueue<V>> getTasksQueues(){
		return map;
	}
	
	/**
	 * 移除任务队列
	 * @param key
	 */
	public void removeTasksQueue(K key) {
		map.remove(key);
	}
}
