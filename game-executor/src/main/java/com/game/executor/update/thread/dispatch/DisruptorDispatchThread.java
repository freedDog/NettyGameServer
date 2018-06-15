package com.game.executor.update.thread.dispatch;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import com.game.executor.common.utils.Loggers;
import com.game.executor.event.CycleEvent;
import com.game.executor.event.EventBus;
import com.game.executor.event.common.IEvent;
import com.game.executor.event.factory.CycleDisruptorEventFactory;
import com.game.executor.event.impl.event.UpdateEvent;
import com.game.executor.update.cache.UpdateEventCacheService;
import com.game.executor.update.pool.DisruptorExecutorService;
import com.game.executor.update.pool.IUpdateExecutor;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;

/**
 * 加入disruptor
 * @author JiangBangMing
 *
 * 2018年6月11日 下午5:33:50
 */
public class DisruptorDispatchThread extends DispatchThread{
	
	private RingBuffer<CycleEvent> ringBuffer;
	
	private int bufferSize=1024*64;
	
	private DisruptorExecutorService disruptorExecutorService;
	
	private BlockingQueue<IEvent> blockingQueue;
	
	private boolean runningFlag=true;
	
	private AtomicLong total;
	
	private int cycleSleepTime;
	private long minCycleTime;
	
	public DisruptorDispatchThread(EventBus eventBus,IUpdateExecutor iUpdateExecutor,int cycleSleepTime,long minCycleTime) {
		super(eventBus);
		this.disruptorExecutorService=(DisruptorExecutorService)iUpdateExecutor;
		this.blockingQueue=new LinkedBlockingQueue<>(bufferSize);
		this.cycleSleepTime=cycleSleepTime;
		this.minCycleTime=minCycleTime;
		this.total=new AtomicLong();
	}
	
	public void initRingBuffer() {
		ringBuffer=RingBuffer.createSingleProducer(new CycleDisruptorEventFactory(), bufferSize, new BlockingWaitStrategy());
	}
	
	public void addUpdateEvent(IEvent event) {
		this.putEvent(event);
	}
	
	public void addCreateEvent(IEvent event) {
		this.putEvent(event);
	}
	
	public void addFinishEvent(IEvent event) {
		this.putEvent(event);
	}
	
	public void putEvent(IEvent event) {
		try {
			this.blockingQueue.put(event);
			total.getAndIncrement();
		}catch(InterruptedException e) {
			Loggers.gameExecutorError.error(e.toString(),e);
		}
	}

	@Override
	public void unpark() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void park() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IUpdateExecutor getiUpdateExecutor() {
		return this.disruptorExecutorService;
	}

	@Override
	public void startup() {
		initRingBuffer();
	}
	
	public void shutDown() {
		this.runningFlag=false;
	}
	
	public void run() {
		while(runningFlag) {
			long cycleSize=total.get();
			
			int i=0;
			long startTime=System.nanoTime();
			while(i<cycleSize) {
				CycleEvent cycleEvent=null;
				try {
					cycleEvent=(CycleEvent)blockingQueue.take();
					dispatch(cycleEvent);
				}catch(InterruptedException e) {
					Loggers.gameExecutorError.error(e.toString(),e);
				}
				i++;
			}
			//准备睡觉
			checkSleep(startTime);
		}
	}
	
	public void checkSleep(long startTime) {
		long notifyTime=System.nanoTime();
		long diff=(int)(notifyTime-startTime);
		if(diff<minCycleTime&&diff>0) {
			try {
				Thread.sleep(cycleSleepTime,(int)(diff%999999));
			}catch(Throwable e) {
				Loggers.gameExecutorError.error(e.toString(),e);
			}
		}
	}
	
	public void dispatch(IEvent event) {
		ringBuffer=disruptorExecutorService.getDispatchRingBuffer();
		long next=ringBuffer.next();
		CycleEvent cycleEvent=(CycleEvent)event;
		CycleEvent destEvent=ringBuffer.get(next);
		destEvent.setId(cycleEvent.getId());
		destEvent.setEventType(event.getEventType());
		destEvent.setParams(event.getParams());
		destEvent.setInitFlag(cycleEvent.isInitFlag());
		destEvent.setUpdateAliveFlag(cycleEvent.isUpdateAliveFlag());
		destEvent.setUpdateExcutorIndex(cycleEvent.getUpdateExcutorIndex());
		
		ringBuffer.publish(next);
		total.getAndDecrement();
		if(event instanceof UpdateEvent) {
			UpdateEvent updateEvent=(UpdateEvent)event;
			UpdateEventCacheService.releaseUpdateEvent(updateEvent);
		}
		
		
	}
	
	public RingBuffer<CycleEvent> getRingBuffer() {
		return ringBuffer;
	}

	public void setRingBuffer(RingBuffer<CycleEvent> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	public DisruptorExecutorService getDisruptorExecutorService() {
		return disruptorExecutorService;
	}

	public void setDisruptorExecutorService(DisruptorExecutorService disruptorExecutorService) {
		this.disruptorExecutorService = disruptorExecutorService;
	}
	
	
}
