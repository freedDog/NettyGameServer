package com.game.executor.update.thread.update.bind;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import com.game.executor.common.utils.Constants;
import com.game.executor.common.utils.Loggers;
import com.game.executor.event.EventBus;
import com.game.executor.event.EventParam;
import com.game.executor.event.impl.event.UpdateEvent;
import com.game.executor.update.cache.UpdateEventCacheService;
import com.game.executor.update.entity.IUpdate;
import com.game.executor.update.pool.excutor.BindThreadUpdateExecutorService;
import com.game.executor.update.thread.dispatch.DispatchThread;

/**
 * 线程一旦启动不会停止，使用 arrayblockqueue进行阻塞 fetchUpdates,
 * 并且通过加入一个null update 来进行wakeup
 * @author JiangBangMing
 *
 * 2018年6月11日 下午3:49:31
 */
public class BindingUpdateThread extends AbstractBindingUpdateThread{
	
	private Queue<IUpdate> iUpdates;
	//这里会用来阻塞
	private BlockingQueue<IUpdate> fetchUpdates;
	
	private int fetchSize;
	private int updateSize;
	
	private List<IUpdate> finishList;
	
	private BindThreadUpdateExecutorService bindThreadUpdateExecutorService;
	
	
	public BindingUpdateThread(BindThreadUpdateExecutorService bindThreadUpdateExecutorService,DispatchThread dispatchThread,Queue<IUpdate> iUpdates,BlockingQueue<IUpdate> fetchUpdates) {
		super(dispatchThread,dispatchThread.getEventBus());
		this.bindThreadUpdateExecutorService=bindThreadUpdateExecutorService;
		this.iUpdates=iUpdates;
		this.fetchUpdates=fetchUpdates;
		this.finishList=new ArrayList<IUpdate>();
	}
	
	@Override
	public void run() {
		for(;;) {
			this.fetchUpdates();
			for(;;) {
				try {
					IUpdate excutorUpdate=this.fetchUpdates.take();
					if(excutorUpdate!=null) {
						if(excutorUpdate==BindThreadUpdateExecutorService.nullWeakUpUpdate) {
							continue;
						}
						excutorUpdate.update();
						this.updateSize++;
						this.finishList.add(excutorUpdate);
					}else {
						break;
					}
				}catch(Exception e) {
					Loggers.gameExecutorError.error(e.toString(),e);
					break;
				}
				if(this.updateSize==this.fetchSize) {
					this.sendFinishList();
					break;
				}
			}
			this.cleanFetch();
			//这里会运行的太快，需要阻塞
			try {
				this.fetchUpdates.take();
			}catch (InterruptedException e) {
				Loggers.gameExecutorError.error(e.toString(),e);
			}
		}
	}


	public void cleanFetch() {
		fetchSize=0;
		updateSize=0;
	}
	
	public void fetchUpdates() {
		Iterator<IUpdate> iUpdateIterator=iUpdates.iterator();
		while(iUpdateIterator.hasNext()) {
			fetchUpdates.add(iUpdateIterator.next());
			fetchSize++;
		}
	}
	
	public void sendFinishList() {
		//事件总线增加更新通知
		for(IUpdate excutorUpdate:this.finishList) {
			this.sendFinish(excutorUpdate);
		}
		this.finishList.clear();
	}
	public void sendFinish(IUpdate excutorUpdate) {
		//如果生命周期结束了，直接进行销毁
		if(!excutorUpdate.isActive()) {
			this.bindThreadUpdateExecutorService.removeTaskQueue(excutorUpdate);
		}
		//事件总线增加更新完成通知
		EventParam<IUpdate> params=new EventParam<IUpdate>(excutorUpdate);
		UpdateEvent updateEvent=UpdateEventCacheService.createUpdateEvent();
		updateEvent.setEventType(Constants.EventTypeConstans.updateEventType);
		updateEvent.setId(excutorUpdate.getUpdateId());
		updateEvent.setParams(params);
		updateEvent.setUpdateAliveFlag(excutorUpdate.isActive());
		getEventBus().addEvent(updateEvent);
	}
	
	public void addUpdate(IUpdate iUpdate) {
		this.iUpdates.add(iUpdate);
	}
}
