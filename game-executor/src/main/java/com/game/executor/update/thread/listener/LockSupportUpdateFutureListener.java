package com.game.executor.update.thread.listener;

import java.util.concurrent.locks.LockSupport;

import com.game.executor.common.utils.Constants;
import com.game.executor.common.utils.Loggers;
import com.game.executor.event.EventParam;
import com.game.executor.event.impl.event.UpdateEvent;
import com.game.executor.update.cache.UpdateEventCacheService;
import com.game.executor.update.entity.IUpdate;
import com.game.executor.update.thread.update.LockSupportUpdateFuture;
import com.snowcattle.future.ITaskFuture;
import com.snowcattle.future.ITaskFutureListener;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月16日 下午12:38:58
 */
public class LockSupportUpdateFutureListener implements ITaskFutureListener{

	@Override
	public void operationComplete(ITaskFuture iTaskFuture) throws Exception {
		if(Loggers.gameExecutorUtil.isDebugEnabled()) {
			IUpdate iUpdate=(IUpdate)iTaskFuture.get();
			Loggers.gameExecutorUtil.debug("update complete event id "+iUpdate.getUpdateId());
		}
		LockSupportUpdateFuture lockSupportUpdateFuture=(LockSupportUpdateFuture)iTaskFuture;
		IUpdate iUpdate=(IUpdate)iTaskFuture.get();
		
		//事件总线增加更新完成通知
		EventParam<IUpdate> params=new EventParam<IUpdate>(iUpdate);
		UpdateEvent updateEvent=UpdateEventCacheService.createUpdateEvent();
		updateEvent.setEventType(Constants.EventTypeConstans.updateEventType);
		updateEvent.setId(iUpdate.getUpdateId());
		updateEvent.setParams(params);
		updateEvent.setUpdateAliveFlag(iUpdate.isActive());
		lockSupportUpdateFuture.getDispatchThread().addUpdateEvent(updateEvent);
		//解锁
		LockSupport.unpark(lockSupportUpdateFuture.getDispatchThread());
	}

}
