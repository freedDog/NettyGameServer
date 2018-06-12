package com.game.executor.event.impl.listener;

import com.game.executor.common.utils.Constants;
import com.game.executor.event.AbstractEventListener;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月12日 上午10:36:25
 */
public class ReadyCreateEventListener extends AbstractEventListener{

	@Override
	public void initEventType() {
		register(Constants.EventTypeConstans.readyCreateEventType);
	}

}
