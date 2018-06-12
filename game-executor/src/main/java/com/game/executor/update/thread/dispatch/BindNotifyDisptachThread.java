package com.game.executor.update.thread.dispatch;

import com.game.executor.event.EventBus;
import com.game.executor.update.pool.IUpdateExecutor;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月16日 下午12:44:51
 */
public class BindNotifyDisptachThread extends LockSupportDisptachThread{

	public BindNotifyDisptachThread(EventBus eventBus, IUpdateExecutor iUpdateExecutor, int cycleSleepTime,
			long minCycleTime) {
		super(eventBus, iUpdateExecutor, cycleSleepTime, minCycleTime);
	}

}
