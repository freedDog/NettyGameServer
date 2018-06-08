package com.game.service.message.factory;

import org.springframework.stereotype.Service;

import com.game.message.logic.tcp.common.CommonErrorResponseServerMessage;
import com.game.message.logic.tcp.common.CommonResponseServerMessage;
import com.game.service.message.AbstractNetMessage;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月6日 上午11:21:52
 */
@Service
public class TcpMessageFactory implements ITcpMessageFactory {

	@Override
	public AbstractNetMessage createCommonErrorResponseMessage(int serial, int state, String tip) {
		CommonErrorResponseServerMessage abstractNetMessage=(CommonErrorResponseServerMessage)createCommonErrorResponseMessage(serial, state);
		abstractNetMessage.setArg(tip);
		return abstractNetMessage;
	}

	@Override
	public AbstractNetMessage createCommonErrorResponseMessage(int serial, int state) {
		CommonErrorResponseServerMessage commonErrorResponseServerMessage=new CommonErrorResponseServerMessage();
		commonErrorResponseServerMessage.setSerial(serial);
		commonErrorResponseServerMessage.setState(state);
		return commonErrorResponseServerMessage;
	}

	@Override
	public AbstractNetMessage createCommonResponseMessage(int serial) {
		CommonResponseServerMessage commonResponseServerMessage=new CommonResponseServerMessage();
		commonResponseServerMessage.setSerial(serial);
		return commonResponseServerMessage;
	}

}
