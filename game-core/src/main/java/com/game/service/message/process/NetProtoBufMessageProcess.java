package com.game.service.message.process;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;

import com.game.common.IUpdatable;
import com.game.common.constant.GlobalConstants;
import com.game.common.constant.Loggers;
import com.game.service.message.AbstractNetMessage;
import com.game.service.message.AbstractNetProtoBufMessage;
import com.game.service.net.tcp.session.NettySession;

/**
 * 消息处理器
 * @author JiangBangMing
 *
 * 2018年6月1日 上午11:29:09
 */
public class NetProtoBufMessageProcess implements INetProtoBufMessageProcess,IUpdatable {
	protected static final Logger logger=Loggers.sessionLogger;
	protected static final Logger statLog=Loggers.serverStatusStatistics;
	/**消息的处理总数*/
	protected long statisticsMessageCount=0;
	/**
	 * 网络消息处理队列
	 */
	private Queue<AbstractNetProtoBufMessage> netMessagesQueue;
	private NettySession nettySession;
	/**中断消息处理*/
	protected boolean suspendedProcess;
	
	public NetProtoBufMessageProcess(NettySession nettySession) {
		this.netMessagesQueue=new ConcurrentLinkedQueue<AbstractNetProtoBufMessage>();
		this.nettySession=nettySession;
	}
	
	public void processNetMessage() {
		int i=0;
		AbstractNetProtoBufMessage message=null;
		while(!this.isSuspendedProcess()&&(message=this.netMessagesQueue.poll())!=null&&i<GlobalConstants.Constants.session_prcoss_message_max_size){
			i++;
			long begin=0;
			if(logger.isInfoEnabled()) {
				begin=System.nanoTime();
			}
		}
	}
	
	public boolean update() {
		try {
			this.processNetMessage();
		}catch(Exception e) {
			Loggers.errorLogger.error(e.toString(),e);
		}
		return false;
	}

	

	public void addnetMessage(AbstractNetMessage abstractNetMessage) {
		// TODO Auto-generated method stub
		
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

	public boolean isSuspendedProcess() {
		return suspendedProcess;
	}

	public void setSuspendedProcess(boolean suspendedProcess) {
		this.suspendedProcess = suspendedProcess;
	}
	
}
