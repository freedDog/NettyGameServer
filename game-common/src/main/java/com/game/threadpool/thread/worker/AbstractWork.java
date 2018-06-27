package com.game.threadpool.thread.worker;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月27日 下午12:42:23
 */
public abstract class AbstractWork implements Runnable{

	
	private TasksQueue<AbstractWork> tasksQueue;

	public TasksQueue<AbstractWork> getTasksQueue() {
		return tasksQueue;
	}

	public void setTasksQueue(TasksQueue<AbstractWork> tasksQueue) {
		this.tasksQueue = tasksQueue;
	}
	
	
}
