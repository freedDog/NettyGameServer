package com.game.executor.event.common;

import com.game.executor.event.EventParam;
import com.game.executor.event.EventType;

/**
 * 事件定义
 * @author JiangBangMing
 *
 * 2018年6月6日 下午5:41:58
 */
public interface IEvent {

    public void setEventType(EventType eventType);
    public EventType getEventType();
    public EventParam[] getParams();
    public void setParams(EventParam... eventParams);
    public void call();
}
