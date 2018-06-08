package com.game.service.lookup;
/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月4日 上午10:58:26
 */

import com.game.service.net.tcp.session.NettySession;
import com.game.service.net.tcp.session.NettyTcpSession;

public interface IChannleLookUpService {
	/**
	 *查找 
	 * @param sessionId
	 * @return
	 */

	public NettySession lookup(long sessionId);

	/**
	 * 增加
	 * @param nettyTcpSession
	 * @return
	 */
	public boolean addNettySession(NettyTcpSession nettyTcpSession);
	/**
	 * 移除
	 * @param nettyTcpSession
	 * @return
	 */
	public boolean removenettySession(NettyTcpSession nettyTcpSession);
}
