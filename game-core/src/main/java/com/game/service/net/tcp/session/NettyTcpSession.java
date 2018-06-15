package com.game.service.net.tcp.session;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.IUpdatable;
import com.game.common.exception.NetMessageException;
import com.game.service.message.AbstractNetMessage;
import com.game.service.message.process.NetProtoBufMessageProcess;
import com.game.service.uuid.LongIdGenerator;

import io.netty.channel.Channel;

/**
 * netty tcp的session
 * @author JiangBangMing
 *
 * 2018年6月1日 上午10:55:31
 */
public class NettyTcpSession extends NettySession implements IUpdatable{
	
	private long sessionId;
	/**
	 * 消息发送
	 */
	private NettyTcpNetMessageSender nettyTcpNetMessageSender;
	/**
	 * 消息处理器
	 */
	private NetProtoBufMessageProcess netProtoBufMessageProcess;
	/**
	 * 网络状态检查
	 */
	private TcpNetStateUpdate tcpNetStateUpdate;
	/**
	 * 网络消息切换开关，当玩家进入房间的时候，关闭开关，所有消息处理放入房间内，保证房间内协议处理单线程
	 */
	private volatile boolean netMessageProcessSwitch=true;
	public NettyTcpSession(Channel channel) {
		super(channel);
		LongIdGenerator longIdGenerator=LocalMananger.getInstance().getLocalSpringBeanManager().getLongIdGenerator();
		sessionId=longIdGenerator.generateId();
		nettyTcpNetMessageSender=new NettyTcpNetMessageSender(this);
		netProtoBufMessageProcess=new NetProtoBufMessageProcess(this);
		tcpNetStateUpdate=new TcpNetStateUpdate();
	}
	/**
	 * 增加消息处理切换
	 * @param switchFlag
	 */
	public void processNetMessage(boolean switchFlag) {
		if(netMessageProcessSwitch||switchFlag) {
			netProtoBufMessageProcess.update();
		}
	}
	public void close() throws NetMessageException{
		//设置网络状态
		this.tcpNetStateUpdate.setDisconnecting();
		this.netProtoBufMessageProcess.close();
		this.nettyTcpNetMessageSender.close();
	}
	@Override
	public boolean update() {
		processNetMessage(false);
		tcpNetStateUpdate.update();
		return false;
	}
	/**
	 * 添加消息
	 * @param abstractNetMessage
	 */
	public void addNetMessage(AbstractNetMessage abstractNetMessage) {
		this.netProtoBufMessageProcess.addnetMessage(abstractNetMessage);
	}
	
	public long getSessionId() {
		return sessionId;
	}
	
	public NettyTcpNetMessageSender getNettyTcpNetMessageSender() {
		return nettyTcpNetMessageSender;
	}
	public void setNettyTcpNetMessageSender(NettyTcpNetMessageSender nettyTcpNetMessageSender) {
		this.nettyTcpNetMessageSender = nettyTcpNetMessageSender;
	}
	public NetProtoBufMessageProcess getNetProtoBufMessageProcess() {
		return netProtoBufMessageProcess;
	}
	public void setNetProtoBufMessageProcess(NetProtoBufMessageProcess netProtoBufMessageProcess) {
		this.netProtoBufMessageProcess = netProtoBufMessageProcess;
	}
	public TcpNetStateUpdate getTcpNetStateUpdate() {
		return tcpNetStateUpdate;
	}
	public void setTcpNetStateUpdate(TcpNetStateUpdate tcpNetStateUpdate) {
		this.tcpNetStateUpdate = tcpNetStateUpdate;
	}
	public boolean isNetMessageProcessSwitch() {
		return netMessageProcessSwitch;
	}
	public void setNetMessageProcessSwitch(boolean netMessageProcessSwitch) {
		this.netMessageProcessSwitch = netMessageProcessSwitch;
	}
	
	
	
}
