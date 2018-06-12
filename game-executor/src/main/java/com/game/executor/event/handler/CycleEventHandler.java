package com.game.executor.event.handler;

import com.game.executor.event.CycleEvent;
import com.game.executor.event.EventBus;
import com.lmax.disruptor.EventReleaseAware;
import com.lmax.disruptor.EventReleaser;
import com.lmax.disruptor.WorkHandler;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月9日 上午2:04:33
 */
public class CycleEventHandler implements WorkHandler<CycleEvent>,EventReleaseAware {
	
	private EventReleaser eventReleaser;
	
	private EventBus eventBus;
	
	private EventReleaser eventRelease;

	public CycleEventHandler(EventBus eventBus) {
		this.eventBus=eventBus;
	}
	
	@Override
	public void setEventReleaser(EventReleaser eventReleaser) {
		this.eventRelease=eventReleaser;
	}

	@Override
	public void onEvent(CycleEvent cycleEvent) throws Exception {
		eventBus.handleSingleEvent(cycleEvent);
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	
}
