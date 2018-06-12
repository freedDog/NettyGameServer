package com.game.executor.event.impl.listener;

import com.game.executor.common.utils.Constants;
import com.game.executor.event.AbstractEventListener;

/**
 * 完成监听器
 * @author JiangBangMing
 *
 * 2018年6月11日 下午12:28:28
 */
public class FinishEventListener extends AbstractEventListener{

	@Override
	public void initEventType() {
		register(Constants.EventTypeConstans.finishEventType);
	}

}
