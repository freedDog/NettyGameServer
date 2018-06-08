package com.game.service.message.factory;

import com.game.service.message.AbstractNetMessage;

/**
 *协议工厂
 * @author JiangBangMing
 *
 * 2018年6月6日 上午11:20:06
 */
public interface ITcpMessageFactory {

	public AbstractNetMessage createCommonErrorResponseMessage(int serial,int state,String tip);
	public AbstractNetMessage createCommonErrorResponseMessage(int serial, int state);
    public AbstractNetMessage createCommonResponseMessage(int serial);
}
