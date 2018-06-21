package com.game.service.net.http.handler;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.config.GameServerConfig;
import com.game.logic.net.NetMessageProcessLogic;
import com.game.service.config.GameServerConfigService;
import com.game.service.message.AbstractNetProtoBufMessage;
import com.game.service.message.decoder.NetProtoBufHttpMessageDecoderFactory;
import com.game.service.net.tcp.MessageAttributeEnum;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 默认读取全部，不使用trunked
 * @author JiangBangMing
 *
 * 2018年6月19日 下午5:58:38
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<Object>{
	
	private HttpRequest request;
	/**
	 * 输出日志
	 */
	private final StringBuilder log=new StringBuilder();
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		GameServerConfig gameServerConfig=gameServerConfigService.getGameServerConfig();
		boolean keepAlive=false;
		if(msg instanceof HttpRequest) {
			//记录下request
			request=(HttpRequest)msg;
			keepAlive=HttpUtil.isKeepAlive(request);
			if(request.getMethod()!=HttpMethod.POST) {
				throw new IllegalStateException("请求不是GET请求");
			}
			
			if(HttpUtil.is100ContinueExpected(request)) {
				send100Continue(ctx);
			}
			
			if(gameServerConfig.isDevelopModel()) {
                log.setLength(0);
                log.append("WELCOME TO THE WILD WILD WEB SERVER\r\n");
                log.append("===================================\r\n");

                log.append("VERSION: ").append(request.protocolVersion()).append("\r\n");
                log.append("HOSTNAME: ").append(request.headers().get(HttpHeaderNames.HOST, "unknown")).append("\r\n");
                log.append("REQUEST_URI: ").append(request.uri()).append("\r\n\r\n");

                HttpHeaders headers = request.headers();
                if (!headers.isEmpty()) {
                    for (Map.Entry<String, String> h : headers) {
                        CharSequence key = h.getKey();
                        CharSequence value = h.getValue();
                        log.append("HEADER: ").append(key).append(" = ").append(value).append("\r\n");
                    }
                    log.append("\r\n");
                }

                QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
                Map<String, List<String>> params = queryStringDecoder.parameters();
                if (!params.isEmpty()) {
                    for (Map.Entry<String, List<String>> p : params.entrySet()) {
                        String key = p.getKey();
                        List<String> vals = p.getValue();
                        for (String val : vals) {
                            log.append("PARAM: ").append(key).append(" = ").append(val).append("\r\n");
                        }
                    }
                    log.append("\r\n");
                }

                appendDecoderResult(log, request);
			}
		}
		if(msg instanceof HttpContent) {
			HttpContent content=(HttpContent)msg;
			ByteBuf buf=content.content();
			if(gameServerConfig.isDevelopModel()) {
                if (buf.isReadable()) {
                    log.append("CONTENT: ");
                    log.append(buf.toString(CharsetUtil.UTF_8));
                    log.append("\r\n");
                    appendDecoderResult(log, request);
                }				
			}
			//开始解析
			NetProtoBufHttpMessageDecoderFactory netProtoBufHttpMessageDecoderFactory=LocalMananger.getInstance().getLocalSpringBeanManager().getNetProtoBufHttpMessageDecoderFactory();
			AbstractNetProtoBufMessage netProtoBufMessage=netProtoBufHttpMessageDecoderFactory.praseMessage(buf);
			
			//封装属性
			netProtoBufMessage.setAttribute(MessageAttributeEnum.DISPATCH_HTTP_REQUEST, request);
			
			//进行处理
			NetMessageProcessLogic netMessageProcessLogic=LocalMananger.getInstance().getLocalSpringBeanManager().getNetMessageProcessLogic();
			HttpResponse httpResponse=netMessageProcessLogic.processMessage(netProtoBufMessage, request);
			writeResponse(httpResponse, ctx);
		}
	}
	
	
	
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		FullHttpResponse response=new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
		ctx.write(response);
	}




	private boolean writeResponse(HttpResponse response,ChannelHandlerContext ctx) {
		// Decide whether to close the connection or not.
		boolean keepAlive=HttpUtil.isKeepAlive(request);
		FullHttpResponse fullHttpResponse=(FullHttpResponse)response;
		response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain; charset=UTF-8");
		response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, fullHttpResponse.content().readableBytes());
		if(keepAlive) {
			// Add keep alive header as per:
            // - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
			response.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
		}
		// Encode the cookie.
		String cookieString=request.headers().get(HttpHeaderNames.COOKIE);
		if(cookieString!=null) {
			Set<io.netty.handler.codec.http.cookie.Cookie> cookies=ServerCookieDecoder.STRICT.decode(cookieString);
			if(!cookies.isEmpty()) {
				 // Reset the cookies if necessary.
				for(io.netty.handler.codec.http.cookie.Cookie cookie:cookies) {
					response.headers().add(HttpHeaderNames.SET_COOKIE,io.netty.handler.codec.http.cookie.ServerCookieEncoder.STRICT.encode(cookie));
				}
			}
		}else {
			// Browser sent no cookie.  Add some.
			response.headers().add(HttpHeaderNames.SET_COOKIE,io.netty.handler.codec.http.cookie.ServerCookieEncoder.STRICT.encode("key1","value1"));
			response.headers().add(HttpHeaderNames.SET_COOKIE,io.netty.handler.codec.http.cookie.ServerCookieEncoder.STRICT.encode("key2","value2"));
		}
		
		
		if(!keepAlive) {
			ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		}else {
			ctx.writeAndFlush(response);
		}
		return keepAlive;
	}
	private void send100Continue(ChannelHandlerContext ctx) {
		FullHttpResponse response=new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
		ctx.write(response);
	}
	
	private static void appendDecoderResult(StringBuilder buf,HttpObject o) {
		DecoderResult result=o.decoderResult();
		if(result.isSuccess()) {
			return;
		}
		
		buf.append("...WITH DECODER FAILURE: ");
		buf.append(result.cause());
		buf.append("\r\n");
	}
}
