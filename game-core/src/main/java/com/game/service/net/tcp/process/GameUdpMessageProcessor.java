package com.game.service.net.tcp.process;

import org.slf4j.Logger;

import com.game.bootstrap.GameServerRuntime;
import com.game.common.constant.Loggers;
import com.game.service.message.AbstractNetMessage;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月1日 下午4:43:14
 */
public class GameUdpMessageProcessor implements IMessageProcessor {

	protected static final Logger logger=Loggers.msgLogger;
	/**主消息处理器，处理服务器内部消息，玩家不属于任何场景时发送的消息*/
	private IMessageProcessor mainMessageProcessor;
	
	public GameUdpMessageProcessor(IMessageProcessor messageProcessor) {
		mainMessageProcessor=messageProcessor;
	}
	@Override
	public void start() {
		mainMessageProcessor.start();
	}

	@Override
	public void stop() {
		mainMessageProcessor.stop();
	}
    /**
     * <pre>
     * 1、服务器内部消息、玩家不属于任何场景时发送的消息，单独一个消息队列进行处理
     * 2、玩家在场景中发送过来的消息，添加到玩家的消息队列中，在场景的线程中进行处理
     * </pre>
     */
	@Override
	public void put(AbstractNetMessage msg) {
		if(!GameServerRuntime.isOpen()) {
			logger.info("【Receive but will not process because server not open】"+msg);
			return;
		}
		mainMessageProcessor.put(msg);
	}

	@Override
	public boolean isFull() {
		return mainMessageProcessor.isFull();
	}
	
}
