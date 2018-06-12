package com.game.executor.event.impl.listener;

import com.game.executor.common.utils.Constants;
import com.game.executor.event.AbstractEventListener;

/**
 * 创建监听器
 * @author JiangBangMing
 *
 * 2018年6月11日 下午12:13:19
 */
public class CreateEventListener extends AbstractEventListener{

	@Override
	public void initEventType() {
		register(Constants.EventTypeConstans.createEventType);
	}

}
