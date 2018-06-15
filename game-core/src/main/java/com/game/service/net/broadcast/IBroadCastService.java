package com.game.service.net.broadcast;

import com.game.service.message.AbstractNetMessage;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月12日 下午1:48:48
 */
public interface IBroadCastService {

	public void broadcastMessage(long sessionId,AbstractNetMessage netMessage);
}
