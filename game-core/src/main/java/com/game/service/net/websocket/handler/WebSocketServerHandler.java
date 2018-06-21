package com.game.service.net.websocket.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月19日 下午9:49:17
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<HttpRequest>{

	private static final String WEBSOCKET_PATH="/websocket";
	
	private WebSocketServerHandshaker handshaker;
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
		handleHttpRequest(ctx, msg);
	}
	
	
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}



	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	

	public WebSocketServerHandshaker getHandshaker() {
		return handshaker;
	}



	public void setHandshaker(WebSocketServerHandshaker handshaker) {
		this.handshaker = handshaker;
	}



	private void handleHttpRequest(ChannelHandlerContext ctx,HttpRequest req) {
		if(!req.decoderResult().isSuccess()) {
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
			return;
		}
		
		if(req.method()!=GET) {
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN));
			return;
		}
		
		if("/favicon.ico".equals(req.uri())||"/".equals(req.uri())) {
			FullHttpResponse res=new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
			sendHttpResponse(ctx, req, res);
			return;
		}
		
		WebSocketServerHandshakerFactory wsFactory=new WebSocketServerHandshakerFactory(getWebSocketLocation(req), null,true,5*1024*1024);
		handshaker=wsFactory.newHandshaker(req);
		if(null==handshaker) {
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
		}else {
			handshaker.handshake(ctx.channel(), req);
		}
	}
	
	private static void sendHttpResponse(ChannelHandlerContext ctx,HttpRequest req,FullHttpResponse res) {
		if(res.status().code()!=200) {
			ByteBuf buf=Unpooled.copiedBuffer(res.status().toString(),CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
			HttpUtil.setContentLength(res, res.content().readableBytes());
		}
		
		ChannelFuture f=ctx.channel().writeAndFlush(res);
		if(!HttpUtil.isKeepAlive(req)||res.status().code()!=200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}
	
	private static String getWebSocketLocation(HttpRequest req) {
		String location=req.headers().get(HttpHeaderNames.HOST)+WEBSOCKET_PATH;
		return "ws://"+location;
	}

}
