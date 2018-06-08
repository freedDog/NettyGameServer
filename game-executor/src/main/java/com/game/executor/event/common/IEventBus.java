package com.game.executor.event.common;

import com.game.executor.event.AbstractEventListener;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月6日 下午5:43:57
 */
public interface IEventBus {

    public void addEventListener(AbstractEventListener listene);
    public void removeEventListener(AbstractEventListener listene);
    public void clearEventListener();
    public void addEvent(IEvent event);
    public void handleEvent();
    public void handleSingleEvent(IEvent event) throws Exception;
    public void clearEvent();
    public void clear();
}
