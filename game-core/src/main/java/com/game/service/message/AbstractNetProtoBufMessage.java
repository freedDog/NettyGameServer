package com.game.service.message;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.game.common.annotation.MessageCommandAnnotation;
import com.game.common.exception.CodecException;


/**
 * 需要重新读取body
 * @author JiangBangMing
 *
 * 2018年6月1日 上午11:33:59
 */
public abstract class AbstractNetProtoBufMessage extends AbstractNetMessage{
	
	public AbstractNetProtoBufMessage() {
		this.setNetMessageHead(new NetMessageHead());
		this.setNetMessageBody(new NetMessageBody());
	}
	protected void initHeadCmd() {
		//设置包头
		MessageCommandAnnotation messageCommandAnnotation=this.getClass().getAnnotation(MessageCommandAnnotation.class);
		if(messageCommandAnnotation!=null) {
			this.getNetMessageHead().setCmd((short)messageCommandAnnotation.command());
		}
	}
	/**解析protobuf协议*/
	public abstract void decoderNetProtoBufMessageBody() throws CodecException,Exception;
	/**释放message的body*/
	public void releaseMessageBody() throws CodecException,Exception{
		this.getNetMessageBody().setBytes(null);
	}
	
	public abstract void release() throws CodecException;
	
	public abstract void encodeNetProtoBufMessageBody() throws CodecException,Exception;
	
	public void setSerial(int serial) {
		this.getNetMessageHead().setSerial(serial);
	}
	public String toString() {
		return this.getClass().getSimpleName()+": commandId="+this.getNetMessageHead().getCmd();
	}
	public String toAllInfoString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE).replaceAll("\n","");
	}
	
}
