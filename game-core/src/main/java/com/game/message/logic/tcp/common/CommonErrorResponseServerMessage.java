package com.game.message.logic.tcp.common;

import com.game.common.annotation.MessageCommandAnnotation;
import com.game.common.exception.CodecException;
import com.game.common.util.StringUtils;
import com.game.message.auto.common.CommonMessageProBuf;
import com.game.service.message.AbstractNetProtoBufTcpMessage;
import com.game.service.message.command.MessageCommandIndex;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月6日 上午10:46:05
 */
@MessageCommandAnnotation(command=MessageCommandIndex.COMMON_ERROR_RESPONSE_MESSAGE)
public class CommonErrorResponseServerMessage  extends AbstractNetProtoBufTcpMessage{

	/**
	 * 状态麻
	 */
	private int state;
	/**
	 * 特殊提示
	 */
	private String arg;
	@Override
	public void decoderNetProtoBufMessageBody() throws CodecException, Exception {
		byte[] bytes=getNetMessageBody().getBytes();
		CommonMessageProBuf.CommonErrorResponseServerProBuf req=CommonMessageProBuf.CommonErrorResponseServerProBuf.parseFrom(bytes);
		this.state=req.getState();
		this.arg=req.getArg();
	}

	@Override
	public void release() throws CodecException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encodeNetProtoBufMessageBody() throws CodecException, Exception {
		CommonMessageProBuf.CommonErrorResponseServerProBuf.Builder builder=CommonMessageProBuf.CommonErrorResponseServerProBuf.newBuilder();
		if(!StringUtils.isEmpty(arg)) {
			builder.setArg(arg);
		}
		builder.setState(state);
		byte[] bytes=builder.build().toByteArray();
		getNetMessageBody().setBytes(bytes);
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getArg() {
		return arg;
	}

	public void setArg(String arg) {
		this.arg = arg;
	}
	
	
}
