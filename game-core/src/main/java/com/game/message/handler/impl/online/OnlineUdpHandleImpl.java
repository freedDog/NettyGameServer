package com.game.message.handler.impl.online;

import com.game.common.annotation.MessageCommandAnnotation;
import com.game.message.handler.AbstractMessageHandler;
import com.game.message.logic.udp.online.OnlineHeartClientUDPMessage;
import com.game.service.message.AbstractNetMessage;
import com.game.service.message.command.MessageCommandIndex;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月6日 下午12:04:33
 */
public class OnlineUdpHandleImpl extends AbstractMessageHandler{

    @MessageCommandAnnotation(command = MessageCommandIndex.ONLINE_HEART_CLIENT_UDP_MESSAGE)
    public AbstractNetMessage handleOnlineHeartClientUdpMessage(OnlineHeartClientUDPMessage message) throws Exception {
        OnlineHeartClientUDPMessage onlineHeartClientUdpMessage = new OnlineHeartClientUDPMessage();
        onlineHeartClientUdpMessage.setId(Short.MAX_VALUE);
        long playerId = 6666;
        int tocken = 333;
        onlineHeartClientUdpMessage.setId(message.getId());
        onlineHeartClientUdpMessage.setPlayerId(playerId);
        onlineHeartClientUdpMessage.setTocken(tocken);
        onlineHeartClientUdpMessage.setReceive(message.getSend());
        return onlineHeartClientUdpMessage;
    }
}
