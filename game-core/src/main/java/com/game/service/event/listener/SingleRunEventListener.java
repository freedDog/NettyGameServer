package com.game.service.event.listener;

import com.game.common.annotation.GlobalEventListenerAnnotation;
import com.game.executor.event.AbstractEventListener;
import com.game.service.event.SingleEventConstants;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月12日 下午12:50:48
 */
@GlobalEventListenerAnnotation
public class SingleRunEventListener extends AbstractEventListener {

	@Override
	public void initEventType() {
		register(SingleEventConstants.singleRunEventType);
	}

}
