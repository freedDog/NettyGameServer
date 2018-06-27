package com.game.executor.event.service;

import com.game.executor.common.utils.Loggers;
import com.game.executor.event.EventBus;
import com.game.executor.event.SingleEvent;
import com.game.threadpool.thread.worker.AbstractWork;

/**
 * 单事件worker
 * @author JiangBangMing
 *
 * 2018年6月7日 下午1:41:52
 */
public class SingleEventWork extends AbstractWork{

	private EventBus eventBus;
	private SingleEvent singleEvent;
	
	public SingleEventWork(EventBus eventBus,SingleEvent singleEvent) {
		this.eventBus=eventBus;
		this.singleEvent=singleEvent;
	}
	@Override
	public void run() {
		try {
			eventBus.handleSingleEvent(singleEvent);
		}catch(Exception e) {
			Loggers.gameExecutorError.error(e.toString(),e);
		}
	}

}
