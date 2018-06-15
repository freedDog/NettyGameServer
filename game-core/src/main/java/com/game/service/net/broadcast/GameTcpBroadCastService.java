package com.game.service.net.broadcast;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.game.bootstrap.manager.LocalMananger;
import com.game.service.lookup.NetTcpSessionLoopUpService;
import com.game.service.message.AbstractNetMessage;
import com.game.service.net.tcp.session.NettySession;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月12日 下午1:49:45
 */
@Service
public class GameTcpBroadCastService implements IBroadCastService{

	@Override
	public void broadcastMessage(long sessionId, AbstractNetMessage netMessage) {
		NetTcpSessionLoopUpService netTcpSessionLoopUpService=LocalMananger.getInstance().getLocalSpringServiceManager().getNetTcpSessionLoopUpService();
		ConcurrentHashMap<Long, NettySession> sessions=netTcpSessionLoopUpService.getSessions();
		for(Map.Entry<Long, NettySession> temp:sessions.entrySet()) {
			long destId=temp.getKey();
			if(destId!=sessionId) {
				NettySession nettySession=temp.getValue();
				try {
					nettySession.write(netMessage);
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}

}
