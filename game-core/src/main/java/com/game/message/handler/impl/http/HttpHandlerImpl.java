package com.game.message.handler.impl.http;

import com.game.common.annotation.MessageCommandAnnotation;
import com.game.message.handler.AbstractMessageHandler;
import com.game.message.logic.http.client.OnlineHeartClientHttpMessage;
import com.game.service.message.AbstractNetMessage;
import com.game.service.message.command.MessageCommandIndex;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月6日 上午11:27:18
 */
public class HttpHandlerImpl extends AbstractMessageHandler{

	@MessageCommandAnnotation(command=MessageCommandIndex.ONLINE_HEART_HTTP_CLIENT_MESSAGE)
	public AbstractNetMessage handleOnlineLoginClientHttpMessage(OnlineHeartClientHttpMessage massage) throws Exception {
		OnlineHeartClientHttpMessage onlineHeartClientHttpMessage=new OnlineHeartClientHttpMessage();
		onlineHeartClientHttpMessage.setId(8);
		return onlineHeartClientHttpMessage;
	}
}
