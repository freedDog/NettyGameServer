package com.game.message.logic.tcp.common;

import com.game.common.annotation.MessageCommandAnnotation;
import com.game.common.exception.CodecException;
import com.game.message.auto.common.CommonMessageProBuf;
import com.game.service.message.AbstractNetProtoBufTcpMessage;
import com.game.service.message.command.MessageCommandIndex;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月6日 上午10:53:04
 */
@MessageCommandAnnotation(command=MessageCommandIndex.COMMON_RESPONSE_MESSAGE)
public class CommonResponseServerMessage extends AbstractNetProtoBufTcpMessage{

	@Override
	public void decoderNetProtoBufMessageBody() throws CodecException, Exception {
		byte[] bytes=getNetMessageBody().getBytes();
		CommonMessageProBuf.CommonErrorResponseServerProBuf req=CommonMessageProBuf.CommonErrorResponseServerProBuf.parseFrom(bytes);
	}

	@Override
	public void release() throws CodecException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encodeNetProtoBufMessageBody() throws CodecException, Exception {
		
		CommonMessageProBuf.CommonErrorResponseServerProBuf.Builder builder=CommonMessageProBuf.CommonErrorResponseServerProBuf.newBuilder();
		byte[] bytes=builder.build().toByteArray();
		getNetMessageBody().setBytes(bytes);
	}

}
