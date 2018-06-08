package com.game.executor.event;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.game.executor.event.common.IEvent;
import com.game.executor.event.common.IEventListener;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月6日 下午5:49:08
 */
public abstract class AbstractEventListener implements IEventListener{

	private Set<EventType> set;
	
	public AbstractEventListener() {
		this.set=new CopyOnWriteArraySet<EventType>();
		initEventType();
	}
	
	public abstract void initEventType();
	
	public void  register(EventType eventType) {
		this.set.add(eventType);
	}
	
	public void unRegister(EventType eventType) {
		this.set.remove(eventType);
	}
	
	public boolean containEventType(EventType eventType) {
		return this.set.contains(eventType);
	}
	
	public void fireEvent(IEvent event) {
		event.call();
	}
	
	public Set<EventType> getSet(){
		return set;
	}
}
