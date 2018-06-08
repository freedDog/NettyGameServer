package com.game.message.handler.impl.common;


import com.game.bootstrap.manager.LocalMananger;
import com.game.common.annotation.MessageCommandAnnotation;
import com.game.message.handler.AbstractMessageHandler;
import com.game.message.logic.tcp.online.client.OnlineHeartClientTcpMessage;
import com.game.service.message.AbstractNetMessage;
import com.game.service.message.command.MessageCommandIndex;
import com.game.service.message.factory.TcpMessageFactory;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月5日 下午9:57:28
 */
public class CommonHandlerImpl extends AbstractMessageHandler{
	@MessageCommandAnnotation(command=MessageCommandIndex.ONLINE_HEART_CLIENT_TCP_MESSAGE)
	public AbstractNetMessage handleOnlineHeartMessage(OnlineHeartClientTcpMessage message) throws Exception{
		TcpMessageFactory messageFactory=LocalMananger.getInstance().getLocalSpringBeanManager().getTcpMessageFactory();
		return messageFactory.createCommonResponseMessage(message.getSerial());
	}

}
