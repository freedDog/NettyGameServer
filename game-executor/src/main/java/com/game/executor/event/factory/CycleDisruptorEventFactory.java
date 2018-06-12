package com.game.executor.event.factory;

import com.game.executor.event.CycleEvent;
import com.lmax.disruptor.EventFactory;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月9日 上午2:00:13
 */
public class CycleDisruptorEventFactory implements EventFactory<CycleEvent>{

	@Override
	public CycleEvent newInstance() {
		return new CycleEvent();
	}

}
