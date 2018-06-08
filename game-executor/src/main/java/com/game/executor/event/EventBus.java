package com.game.executor.event;

import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.game.executor.common.utils.Loggers;
import com.game.executor.event.common.IEvent;
import com.game.executor.event.common.IEventBus;
import com.game.executor.event.common.IEventListener;
import com.game.executor.event.common.constant.EventTypeEnum;
import com.game.executor.update.entity.IUpdate;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月7日 上午12:43:03
 */
public class EventBus implements IEventBus{

	private Map<EventType, Set<AbstractEventListener>> listenerMap;
	
	private Queue<IEvent> events;
	//调用线程size比较费性能，这里采用原子的更新器
	private AtomicInteger size=new AtomicInteger();
	
	public EventBus() {
		this.listenerMap=new ConcurrentHashMap<>();
		this.events=new ConcurrentLinkedQueue<>();
	}
	@Override
	public void addEventListener(AbstractEventListener listene) {
		Set<EventType> sets=listene.getSet();
		for(EventType eventType:sets) {
			if(!listenerMap.containsKey(eventType)) {
				listenerMap.put(eventType, new HashSet<AbstractEventListener>());
			}
			listenerMap.get(eventType).add(listene);
		}
	}

	@Override
	public void removeEventListener(AbstractEventListener listene) {
		Set<EventType> sets=listene.getSet();
		for(EventType eventType:sets) {
			listenerMap.get(eventType).remove(listene);
		}
	}

	@Override
	public void clearEventListener() {
		this.listenerMap.clear();
	}

	@Override
	public void addEvent(IEvent event) {
		this.events.add(event);
		size.getAndIncrement();
	}
	
	public IEvent pollEvent() {
		IEvent event=events.poll();
		if(event!=null) {
			size.getAndDecrement();
		}
		return event;
	}
	@Override
	public void handleEvent() {
		while(!events.isEmpty()) {
			IEvent event=this.pollEvent();
			if(null==event) {
				break;
			}
			try {
				handleSingleEvent(event);
			}catch(Exception e) {
				Loggers.gameExecutorError.error(e.toString(),e);
			}
		}
	}
	/**
	 * 单次超过最大设置需要停止
	 * @param maxSize
	 * @return
	 */
	public int cycle(int maxSize) {
		int i=0;
		while(!events.isEmpty()) {
			IEvent event=this.pollEvent();
			if(null==event) {
				break;
			}
			try {
				this.handleSingleEvent(event);
			}catch(Exception e) {
				Loggers.gameExecutorError.error(e.toString(),e);
			}
			
			i++;
			if(i>maxSize) {
				break;
			}
		}
		return i;
	}
	@Override
	public void handleSingleEvent(IEvent event) throws Exception {
		
		if(Loggers.gameExecutorUtil.isDebugEnabled()) {
			EventParam[] eventParams=event.getParams();
			if(eventParams!=null) {
				if(eventParams[0].getT() instanceof IUpdate) {
					IUpdate iUpdate=(IUpdate)eventParams[0].getT();
					if(event.getEventType().getIndex()<EventTypeEnum.values().length) {
						Loggers.gameExecutorUtil.debug("handler "+EventTypeEnum.values()[event.getEventType().getIndex()]+" id "+iUpdate.getUpdateId()+" dispatch");
					}else {
						Loggers.gameExecutorUtil.debug("handler event type "+event.getEventType().getIndex()+" id "+iUpdate.getUpdateId()+" dispatch");
					}
				}
			}
		}
		EventType eventType=event.getEventType();
		if(listenerMap.containsKey(eventType)) {
			Set<AbstractEventListener> listenerSet=listenerMap.get(eventType);
			for(IEventListener eventListener:listenerSet) {
				if(eventListener.containEventType(event.getEventType())) {
					eventListener.fireEvent(event);
				}
			}
		}
	}

	@Override
	public void clearEvent() {
		events.clear();
	}

	@Override
	public void clear() {
		this.clearEvent();
		this.clearEventListener();
	}
	public int getEventSize() {
		return size.get();
	}
}
