package com.game.bootstrap.manager.spring;
/**
 * 这里都是单例
 * @author JiangBangMing
 *
 * 2018年5月31日 下午8:28:30
 */

import org.springframework.beans.factory.annotation.Autowired;

import com.game.service.message.command.MessageCommandFactory;
import com.game.service.message.decoder.NetProtoBufTcpMessageDecoderFactory;
import com.game.service.message.decoder.NetProtoBufUdpMessageDecoderFactory;
import com.game.service.message.encoder.NetProtoBufTcpMessageEncoderFactory;
import com.game.service.message.encoder.NetProtoBufUdpMessageEncoderFactory;
import com.game.service.message.factory.TcpMessageFactory;
import com.game.service.net.tcp.session.builder.NettyTcpSessionBuilder;
import com.game.service.rpc.serialize.protostuff.ProtostuffSerializeI;
import com.game.service.uuid.LongIdGenerator;

public class LocalSpringBeanManager {
	@Autowired
	private NettyTcpSessionBuilder nettyTcpSessionBuilder;
	@Autowired
	private MessageCommandFactory messageCommandFactory;
	@Autowired
	private NetProtoBufTcpMessageDecoderFactory netProtoBufTcpMessageDecoderFactory;
	@Autowired
	private NetProtoBufUdpMessageDecoderFactory netProtoBufUdpMessageDecoderFactory;
	@Autowired
	private ProtostuffSerializeI protostuffSerialize;
    @Autowired
    private NetProtoBufTcpMessageEncoderFactory netProtoBufTcpMessageEncoderFactory;
    @Autowired
    private NetProtoBufUdpMessageEncoderFactory netProtoBufUdpMessageEncoderFactory;
    @Autowired
    private TcpMessageFactory tcpMessageFactory;
    @Autowired
    private LongIdGenerator longIdGenerator;
	public NettyTcpSessionBuilder getNettyTcpSessionBuilder() {
		return nettyTcpSessionBuilder;
	}

	public void setNettyTcpSessionBuilder(NettyTcpSessionBuilder nettyTcpSessionBuilder) {
		this.nettyTcpSessionBuilder = nettyTcpSessionBuilder;
	}

	public MessageCommandFactory getMessageCommandFactory() {
		return messageCommandFactory;
	}

	public void setMessageCommandFactory(MessageCommandFactory messageCommandFactory) {
		this.messageCommandFactory = messageCommandFactory;
	}

	public NetProtoBufTcpMessageDecoderFactory getNetProtoBufTcpMessageDecoderFactory() {
		return netProtoBufTcpMessageDecoderFactory;
	}

	public void setNetProtoBufTcpMessageDecoderFactory(
			NetProtoBufTcpMessageDecoderFactory netProtoBufTcpMessageDecoderFactory) {
		this.netProtoBufTcpMessageDecoderFactory = netProtoBufTcpMessageDecoderFactory;
	}

	public NetProtoBufUdpMessageDecoderFactory getNetProtoBufUdpMessageDecoderFactory() {
		return netProtoBufUdpMessageDecoderFactory;
	}

	public void setNetProtoBufUdpMessageDecoderFactory(
			NetProtoBufUdpMessageDecoderFactory netProtoBufUdpMessageDecoderFactory) {
		this.netProtoBufUdpMessageDecoderFactory = netProtoBufUdpMessageDecoderFactory;
	}

	public ProtostuffSerializeI getProtostuffSerialize() {
		return protostuffSerialize;
	}

	public void setProtostuffSerialize(ProtostuffSerializeI protostuffSerialize) {
		this.protostuffSerialize = protostuffSerialize;
	}

	public NetProtoBufTcpMessageEncoderFactory getNetProtoBufTcpMessageEncoderFactory() {
		return netProtoBufTcpMessageEncoderFactory;
	}

	public void setNetProtoBufTcpMessageEncoderFactory(
			NetProtoBufTcpMessageEncoderFactory netProtoBufTcpMessageEncoderFactory) {
		this.netProtoBufTcpMessageEncoderFactory = netProtoBufTcpMessageEncoderFactory;
	}

	public NetProtoBufUdpMessageEncoderFactory getNetProtoBufUdpMessageEncoderFactory() {
		return netProtoBufUdpMessageEncoderFactory;
	}

	public void setNetProtoBufUdpMessageEncoderFactory(
			NetProtoBufUdpMessageEncoderFactory netProtoBufUdpMessageEncoderFactory) {
		this.netProtoBufUdpMessageEncoderFactory = netProtoBufUdpMessageEncoderFactory;
	}

	public TcpMessageFactory getTcpMessageFactory() {
		return tcpMessageFactory;
	}

	public void setTcpMessageFactory(TcpMessageFactory tcpMessageFactory) {
		this.tcpMessageFactory = tcpMessageFactory;
	}

	public LongIdGenerator getLongIdGenerator() {
		return longIdGenerator;
	}

	public void setLongIdGenerator(LongIdGenerator longIdGenerator) {
		this.longIdGenerator = longIdGenerator;
	}
	
	
	
}