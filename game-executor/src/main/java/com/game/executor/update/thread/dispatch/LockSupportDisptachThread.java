package com.game.executor.update.thread.dispatch;

import java.util.concurrent.locks.LockSupport;

import com.game.executor.common.utils.Loggers;
import com.game.executor.event.EventBus;
import com.game.executor.update.pool.IUpdateExecutor;

/**
 * 带预置锁的分配器
 * 接受 create,update,finish事件
 * 负责整个调度器的调度，按照bus里面的大小来确定每次循环多少个
 * @author JiangBangMing
 *
 * 2018年6月16日 下午12:45:24
 */
public class LockSupportDisptachThread extends DispatchThread{

	private boolean runningFlag=true;
	private IUpdateExecutor iUpdateExecutor;
	//2140
	private int cycleSleepTime;
	private long minCycleTime;
	
	public LockSupportDisptachThread(EventBus eventBus,IUpdateExecutor iUpdateExecutor,int cycleSleepTime,long minCycleTime) {
		super(eventBus);
		this.iUpdateExecutor=iUpdateExecutor;
		this.cycleSleepTime=cycleSleepTime;
		this.minCycleTime=minCycleTime;
	}
	
	
	@Override
	public void run() {
		while(runningFlag) {
			this.singleCycle(true);
		}
	}

	
	@Override
	public void unpark() {
		LockSupport.unpark(this);
	}

	@Override
	public void park() {
		LockSupport.park(this);
	}

	@Override
	public IUpdateExecutor getiUpdateExecutor() {
		return this.iUpdateExecutor;
	}
	

	public void setiUpdateExecutor(IUpdateExecutor iUpdateExecutor) {
		this.iUpdateExecutor = iUpdateExecutor;
	}


	@Override
	public void startup() {
		
	}
	
	@Override
	public void notifyRun() {
		this.singleCycle(false);
	}


	@Override
	public void shutDown() {
		this.runningFlag=false;
		super.shutDown();
	}


	public void checkSleep(long startTime) {
		long notifyTime=System.nanoTime();
		long diff=(int)(notifyTime-startTime);
		if(diff<minCycleTime&&diff>0) {
			try {
				Thread.sleep(cycleSleepTime,(int)(diff%999999));
			}catch (Throwable e) {
				Loggers.gameExecutorError.error(e.toString(),e);
			}
		}
	}
	
	private void singleCycle(boolean sleepFlag) {
		long startTime=System.nanoTime();
		int cycleSize=getEventBus().getEventSize();
		if(sleepFlag) {
			int size=getEventBus().cycle(cycleSize);
			this.park();
			this.checkSleep(startTime);
		}else {
			int size=getEventBus().cycle(cycleSize);
		}
	}
}
