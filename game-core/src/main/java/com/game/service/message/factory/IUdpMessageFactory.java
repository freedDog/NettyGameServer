package com.game.service.message.factory;

import com.game.service.message.AbstractNetProtoBufUdpMessage;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月6日 上午11:21:36
 */
public interface IUdpMessageFactory {
	  public AbstractNetProtoBufUdpMessage createCommonErrorResponseMessage(int serial, int state);
	  public AbstractNetProtoBufUdpMessage createCommonResponseMessage(int serial);
}
