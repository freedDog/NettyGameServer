package com.game.executor.event.common;

import com.game.executor.event.EventType;

/**
 * 事件监听器
 * @author JiangBangMing
 *
 * 2018年6月6日 下午5:47:29
 */
public interface IEventListener {

	public void register(EventType eventType);
	
	public boolean containEventType(EventType eventType);
	
	public void fireEvent(IEvent event);
}
