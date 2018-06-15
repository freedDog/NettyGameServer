package com.game.logic.net;

import org.springframework.stereotype.Service;

import com.game.common.constant.CommonErrorLogInfo;
import com.game.common.constant.Loggers;
import com.game.common.util.ErrorsUtil;
import com.game.service.message.AbstractNetMessage;
import com.game.service.net.tcp.MessageAttributeEnum;
import com.game.service.net.tcp.process.IMessageProcessor;
import com.game.service.net.tcp.session.NettyTcpSession;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月14日 下午1:09:20
 */
@Service
public class NetMessageTcpDispatchLogic {
	/**处理的消息总数*/
	public long statisticsMessageCount=0;
	
	public void dispatchTcpMessage(AbstractNetMessage msg,IMessageProcessor iMessageProcessor) {
		if(null==msg) {
			if(Loggers.serverStatusStatistics.isWarnEnabled()) {
				Loggers.serverStatusStatistics.warn("[#CORE.QueueMessageExecutorProcessor.process] ["
						+CommonErrorLogInfo.MSG_PRO_ERR_NULL_MSG);
			}
			return;
		}
		long begin=0;
		if(Loggers.serverStatusStatistics.isInfoEnabled()) {
			begin=System.nanoTime();
		}
		statisticsMessageCount++;
		try {
			NettyTcpSession clientSession=(NettyTcpSession)msg.getAttribute(MessageAttributeEnum.DISPATCH_SESSION);
			if(clientSession!=null) {
				clientSession.addNetMessage(msg);
			}else {
				if(Loggers.serverStatusStatistics.isInfoEnabled()) {
					Loggers.serverStatusStatistics.info("session is closed,the message is unDispatch");
				}
			}
		}catch (Exception e) {
			if(Loggers.serverStatusStatistics.isErrorEnabled()) {
				Loggers.serverStatusStatistics.error(ErrorsUtil.error("Error",
						"#.QueueMessageExecutorProcessor.process"," param"),e);
			}
		}finally {
            if (Loggers.serverStatusStatistics.isInfoEnabled()) {
                // 特例，统计时间跨度
                long time = (System.nanoTime() - begin) / (1000 * 1000);
                if (time > 1) {
                    Loggers.serverStatusStatistics.info("#CORE.MSG.PROCESS.DISPATCH_STATICS disptach Message id:"
                            + msg.getCmd() + " Time:"
                            + time + "ms" + " Total:"
                            + statisticsMessageCount);
                }
            }
		}
	}
}
