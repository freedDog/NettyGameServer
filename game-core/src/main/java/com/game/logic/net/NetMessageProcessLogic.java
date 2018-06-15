package com.game.logic.net;
/**
 * 消息的真正处理
 * @author JiangBangMing
 *
 * 2018年6月1日 下午12:15:08
 */

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.constant.Loggers;
import com.game.common.exception.GameHandlerException;
import com.game.common.util.ErrorsUtil;
import com.game.service.message.AbstractNetMessage;
import com.game.service.message.AbstractNetProtoBufMessage;
import com.game.service.message.encoder.NetProtoBufHttpMessageEncoderFactory;
import com.game.service.message.encoder.NetProtoBufTcpMessageEncoderFactory;
import com.game.service.message.facade.GameFacade;
import com.game.service.message.factory.ITcpMessageFactory;
import com.game.service.net.tcp.session.NettySession;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


@Service
public class NetMessageProcessLogic {

	protected static final Logger logger = Loggers.sessionLogger;
	protected static final Logger statLog = Loggers.serverStatusStatistics;

	public void processMessage(AbstractNetMessage message, NettySession nettySession) {
		long begin = System.nanoTime();
		try {
			GameFacade gameFacade = LocalMananger.getInstance().getLocalSpringServiceManager().getGameFacade();
			AbstractNetProtoBufMessage respone = null;
			respone = (AbstractNetProtoBufMessage) gameFacade.dispatch(message);
			if (respone != null) {
				respone.setSerial(message.getNetMessageHead().getSerial());
				nettySession.write(respone);
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				Loggers.errorLogger
						.error(ErrorsUtil.error("Error", "#.QueueMessageExecutorProcessor .process", " param"), e);
			}

			if (e instanceof GameHandlerException) {
				GameHandlerException gameHandlerException = (GameHandlerException) e;
				ITcpMessageFactory iTcpMessageFactory = LocalMananger.getInstance().get(ITcpMessageFactory.class);
				AbstractNetMessage errorMessage = iTcpMessageFactory.createCommonErrorResponseMessage(
						gameHandlerException.getSerial(), gameHandlerException.COMMON_ERROR_STATE);
				try {
					nettySession.write(errorMessage);
				} catch (Exception writeException) {
					Loggers.errorLogger.error(
							ErrorsUtil.error("Error" + "#.QueueMessageExecutorProcessor.writeErrorMessage", "param"),
							e);
				}
			}
		} finally {
			if (logger.isInfoEnabled()) {
				// 特例，统计时间跨度
				long time = (System.nanoTime() - begin) / (1000 * 1000);
				if (time > 1) {
					statLog.info("#CORE.MSG.PROCESS.STATICS Message id:" + message.getNetMessageHead().getCmd()
							+ " Time:" + time + "ms");
				}
			}
		}
	}

	public HttpResponse processMessage(AbstractNetMessage message, HttpRequest request) {
		FullHttpResponse httpResponse = null;
		AbstractNetProtoBufMessage respone = null;
		long begin = System.nanoTime();
		try {
			GameFacade gameFacade = LocalMananger.getInstance().getLocalSpringServiceManager().getGameFacade();
			respone = (AbstractNetProtoBufMessage) gameFacade.dispatch(message);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				Loggers.errorLogger.error(ErrorsUtil.error("Error", "#.QueueMessageExecutorProcessor.process", "param"),
						e);
			}

			if (e instanceof GameHandlerException) {
				GameHandlerException gameHandlerException = (GameHandlerException) e;
				ITcpMessageFactory iTcpMessageFactory = LocalMananger.getInstance().get(ITcpMessageFactory.class);
				AbstractNetMessage errorMessage = iTcpMessageFactory.createCommonErrorResponseMessage(
						gameHandlerException.getSerial(), gameHandlerException.COMMON_ERROR_STATE);
			}
		} finally {
			if (logger.isInfoEnabled()) {
				// 特例，统计时间跨度
				long time = (System.nanoTime() - begin) / (1000 * 1000);
				if (time > 1) {
					statLog.info("#CORE.MSG.PROCESS.STATICS Message id:" + message.getNetMessageHead().getCmd()
							+ " Time:" + time + "ms");
				}
			}
		}

		NetProtoBufHttpMessageEncoderFactory netProtoBufHttpMessageEncoderFactory = LocalMananger.getInstance()
				.getLocalSpringBeanManager().getNetProtoBufHttpMessageEncoderFactory();
		if (respone != null) {
			respone.setSerial(message.getNetMessageHead().getSerial());
			try {
				httpResponse = new DefaultFullHttpResponse(HTTP_1_1, OK,
						Unpooled.copiedBuffer(netProtoBufHttpMessageEncoderFactory.createByteBuf(respone)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return httpResponse;
	}

	public void processWebSocketMessage(AbstractNetMessage message, Channel channel) {
		AbstractNetProtoBufMessage respone = null;
		long begin = System.nanoTime();
		try {
			GameFacade gameFacade = LocalMananger.getInstance().getLocalSpringServiceManager().getGameFacade();
			respone = (AbstractNetProtoBufMessage) gameFacade.dispatch(message);
			if (respone != null) {
				respone.setSerial(message.getNetMessageHead().getSerial());

				NetProtoBufTcpMessageEncoderFactory netProtoBufTcpMessageEncoderFactory = new NetProtoBufTcpMessageEncoderFactory();
				ByteBuf byteBuf = netProtoBufTcpMessageEncoderFactory.createByteBuf(respone);

				BinaryWebSocketFrame binaryWebSocketFrame = new BinaryWebSocketFrame(byteBuf);
				channel.writeAndFlush(binaryWebSocketFrame);
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				Loggers.errorLogger.error(ErrorsUtil.error("Error", "#.QueueMessageExecutorProcessor.process", "param"),
						e);
			}

			if (e instanceof GameHandlerException) {
				GameHandlerException gameHandlerException = (GameHandlerException) e;
				ITcpMessageFactory iTcpMessageFactory = LocalMananger.getInstance().get(ITcpMessageFactory.class);
				AbstractNetMessage errorMessage = iTcpMessageFactory.createCommonErrorResponseMessage(
						gameHandlerException.getSerial(), gameHandlerException.COMMON_ERROR_STATE);
			}
		} finally {
			if (logger.isInfoEnabled()) {
				// 特例，统计时间跨度
				long time = (System.nanoTime() - begin) / (1000 * 1000);
				if (time > 1) {
					statLog.info("#CORE.MSG.PROCESS.STATICS Message id:" + message.getNetMessageHead().getCmd()
							+ " Time:" + time + "ms");
				}
			}
		}
	}
}
