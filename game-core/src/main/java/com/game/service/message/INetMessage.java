package com.game.service.message;
/**
 * 基础协议
 * @author JiangBangMing
 *
 * 2018年5月31日 下午8:38:00
 */
public interface INetMessage {
	public NetMessageHead getNetMessageHead();
	public NetMessageBody getNetMessageBody();
}
