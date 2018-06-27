package com.game.service.net.tcp.process;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.config.GameServerDiffConfig;
import com.game.common.constant.CommonErrorLogInfo;
import com.game.common.constant.GlobalConstants;
import com.game.common.constant.Loggers;
import com.game.logic.net.NetMessageTcpDispatchLogic;
import com.game.service.message.AbstractNetMessage;
import com.game.threadpool.common.utils.ExecutorUtil;
import com.game.threadpool.thread.ThreadNameFactory;

/**
 * 默认不开启worker线程
 * @author JiangBangMing
 *
 * 2018年6月14日 下午12:50:42
 */
public class QueueTcpMessageExecutorProcessor implements ITcpMessageProcessor{

	public static final Logger logger=Loggers.serverStatusStatistics;
	/**消息队列*/
	protected final BlockingQueue<AbstractNetMessage> queue;
	/**消息处理线程停止时剩余的还未处理的消息*/
	private volatile List<AbstractNetMessage> leftQueue;
	/**消息处理线程池*/
	private volatile ExecutorService executorService;
	/**线程池的线程个数*/
	private int executorCoreSize;
	/**是否停止*/
	private volatile boolean stop=false;
	
	private final boolean processLeft;
	
	public QueueTcpMessageExecutorProcessor(boolean processLeft,int executorCoreSize) {
		queue=new LinkedBlockingQueue<AbstractNetMessage>();
		this.processLeft=processLeft;
		this.executorCoreSize=executorCoreSize;
	}
	
	public QueueTcpMessageExecutorProcessor() {
		this(false,0);
	}
	
	/**
	 * 开始消息处理
	 */
	@Override
	public synchronized void start() {
		if(this.executorService!=null) {
			throw new IllegalStateException("The executorService has not been stopped.");
		}
		stop=false;
		ThreadNameFactory factory=new ThreadNameFactory(GlobalConstants.Thread.GAME_MESSAGE_QUEUE_EXCUTE);
		this.executorService=Executors.newFixedThreadPool(executorCoreSize, factory);
		
		GameServerDiffConfig gameServerDiffConfig=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService().getGameServerDiffConfig();
		for(int i=0;i<this.executorCoreSize;i++) {
			this.executorService.execute(new Worker());
		}
		
		logger.info("Message processor executorService started["
				+this.executorService+" with "+this.executorCoreSize
				+" threads]");
	}
	/**
	 * 停止消息处理
	 */
	@Override
	public synchronized void stop() {
		logger.info("Message processor executor "+this+" stopping ...");
		this.stop=true;
		if(this.executorService!=null) {
			ExecutorUtil.shutdownAndAwaitTermination(executorService, 50, TimeUnit.MILLISECONDS);
			this.executorService=null;
		}
		logger.info("Message processor executor "+this+" stopped.");
		
		if(this.processLeft) {
			//将未处理的消息放入到leftQueue中，以备后续处理
			this.leftQueue=new LinkedList<>();
			while(true) {
				AbstractNetMessage _msg=this.queue.poll();
				if(_msg!=null) {
					this.leftQueue.add(_msg);
				}else {
					break;
				}
			}
		}
		this.queue.clear();
	}

	@Override
	public void put(AbstractNetMessage msg) {
		try {
			queue.put(msg);
			if(logger.isDebugEnabled()) {
				logger.debug("put queue size:"+queue.size());
			}
		}catch (InterruptedException e) {
			if(logger.isErrorEnabled()) {
				logger.error(CommonErrorLogInfo.THRAD_ERR_INTERRUPTED,e);
			}
		}
	}

	/**
	 * 达到5000上限时认为满了
	 */
	@Override
	public boolean isFull() {
		return this.queue.size()>5000;
	}
	/**
	 * 取得消息处理器停止后的遗留的消息
	 * @return
	 */
	public List<AbstractNetMessage> getLeftQueue(){
		return this.leftQueue;
	}
	/**
	 * 重置
	 */
	public void resetLeftQueue() {
		this.leftQueue=null;
	}
	
	@Override
	public void directPutTcpMessage(AbstractNetMessage msg) {
		try {
			NetMessageTcpDispatchLogic netMessageTcpDispatchLogic=LocalMananger.getInstance().getLocalSpringBeanManager().getNetMessageTcpDispatchLogic();
			netMessageTcpDispatchLogic.dispatchTcpMessage(msg, this);
		}catch (Exception e) {
			if(logger.isErrorEnabled()) {
				logger.error(CommonErrorLogInfo.THRAD_ERR_INTERRUPTED,e);
			}
		}
	}
	
	/**
	 * 处理具体的消息，每个消息有自己的参数和来源，如果在处理消息的过程中发生异常，则马上将此消息的发送者断掉
	 * @param msg
	 */
	public void process(AbstractNetMessage msg) {
		
	}
	private final class Worker implements Runnable{

		@Override
		public void run() {
			while(!stop) {
				try {
					process(queue.take());
					if(logger.isDebugEnabled()) {
						logger.debug("run queue size:"+queue.size());
					}
				}catch (InterruptedException e) {
					if(logger.isWarnEnabled()) {
						logger.warn("[#CORE.QueueMessageExecutorProcessor.run] [ Stop process]");
					}
					Thread.currentThread().interrupt();
					break;
				}catch (Exception e) {
					e.printStackTrace();
					logger.error(CommonErrorLogInfo.MSG_PRO_ERR_EXP,e);
				}
			}
		}
		
	}
}
