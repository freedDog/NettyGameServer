package com.game.service.event;

import com.game.executor.event.EventType;

public class SingleEventConstants {

	public static final EventType singleRunEventType=new EventType(1001);
	public static final EventType sessionRegister=new EventType(1002);
	public static final EventType sessionUnRegister=new EventType(1003);
}
