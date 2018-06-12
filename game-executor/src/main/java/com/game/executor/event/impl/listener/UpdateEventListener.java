package com.game.executor.event.impl.listener;

import com.game.executor.common.utils.Constants;
import com.game.executor.event.AbstractEventListener;

/**
 * 更新监听器
 * @author JiangBangMing
 *
 * 2018年6月12日 上午10:28:36
 */
public class UpdateEventListener extends AbstractEventListener{

	@Override
	public void initEventType() {
		register(Constants.EventTypeConstans.updateEventType);
	}

}
