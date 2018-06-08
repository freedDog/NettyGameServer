package com.game.service.net.tcp.session;

import com.game.service.message.AbstractNetMessage;

/**
 * 封装会话的业务逻辑
 * @author JiangBangMing
 *
 * 2018年5月31日 下午8:35:24
 */
public interface ISession {
	/**
	 * 判断当前会话是否处于连接状态
	 * @return
	 */
	public boolean isConnected();
	/**
	 * 写
	 * @param msg
	 * @throws Exception
	 */
	public void write(AbstractNetMessage msg) throws Exception;
	/**
	 * 
	 */
	public void close(boolean immediately);
	/**
	 * 出现异常时是否关闭连接
	 * @return
	 */
	public boolean closeOnException();
	
	public void write(byte[] msg) throws Exception;
}
