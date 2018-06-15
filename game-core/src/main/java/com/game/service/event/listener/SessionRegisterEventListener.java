package com.game.service.event.listener;

import com.game.common.annotation.GlobalEventListenerAnnotation;
import com.game.executor.event.AbstractEventListener;
import com.game.service.event.SingleEventConstants;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月12日 下午12:51:55
 */

@GlobalEventListenerAnnotation
public class SessionRegisterEventListener extends AbstractEventListener{

	@Override
	public void initEventType() {
		register(SingleEventConstants.sessionRegister);
	}

}
