package com.game.message.handler.impl.online;

import java.util.concurrent.atomic.AtomicLong;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.annotation.MessageCommandAnnotation;
import com.game.common.constant.Loggers;
import com.game.logic.player.GamePlayer;
import com.game.message.handler.AbstractMessageHandler;
import com.game.message.logic.tcp.online.client.OnlineLoginClientTcpMessage;
import com.game.message.logic.tcp.online.server.OnlineLoginServerTcpMessage;
import com.game.service.lookup.GamePlayerLoopUpService;
import com.game.service.message.AbstractNetMessage;
import com.game.service.message.command.MessageCommandIndex;
import com.game.service.net.tcp.MessageAttributeEnum;
import com.game.service.net.tcp.session.NettyTcpSession;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月6日 上午11:29:38
 */
public class OnlineTcpHandlerImpl extends AbstractMessageHandler{

	private AtomicLong id=new AtomicLong();
	 @MessageCommandAnnotation(command = MessageCommandIndex.ONLINE_LOGIN_TCP_CLIENT_MESSAGE)
	public AbstractNetMessage handleOnlineLoginClientTcpMessage(OnlineLoginClientTcpMessage message) throws Exception {
		OnlineLoginServerTcpMessage onlineLoginServerTcpMessage = new OnlineLoginServerTcpMessage();
        long playerId = 6666 + id.incrementAndGet();
        int tocken = 333;
        onlineLoginServerTcpMessage.setPlayerId(playerId);
        onlineLoginServerTcpMessage.setTocken(tocken);
        if (Loggers.sessionLogger.isDebugEnabled()) {
            Loggers.sessionLogger.debug( "playerId " + playerId + "tocken " + tocken + "login");
        }
        NettyTcpSession clientSesion = (NettyTcpSession) message.getAttribute(MessageAttributeEnum.DISPATCH_SESSION);
        GamePlayer gamePlayer = new GamePlayer(clientSesion.getNettyTcpNetMessageSender(), playerId, tocken);
        GamePlayerLoopUpService gamePlayerLoopUpService = LocalMananger.getInstance().getLocalSpringServiceManager().getGamePlayerLoopUpService();
        gamePlayerLoopUpService.addT(gamePlayer);
        return onlineLoginServerTcpMessage;
	}
}
