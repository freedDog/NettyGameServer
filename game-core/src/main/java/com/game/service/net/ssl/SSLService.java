package com.game.service.net.ssl;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.config.GameServerConfig;
import com.game.common.constant.ServiceName;
import com.game.service.IService;
import com.game.service.config.GameServerConfigService;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * websocket的ssl服务
 * @author JiangBangMing
 *
 * 2018年6月19日 下午8:49:54
 */
public class SSLService implements IService{
	
	private SslContext sslContext;
	@Override
	public String getId() {
		return ServiceName.SSLService;
	}

	@Override
	public void startup() throws Exception {
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		GameServerConfig gameServerConfig=gameServerConfigService.getGameServerConfig();
		boolean webSocketSSLFlag=gameServerConfig.isWebSockectSSLFlag();
		if(webSocketSSLFlag) {
			SelfSignedCertificate ssc=new SelfSignedCertificate();
			sslContext=SslContextBuilder.forServer(ssc.certificate(),ssc.privateKey()).build();
		}
	}

	@Override
	public void shutdown() throws Exception {
		
	}

	public SslContext getSslContext() {
		return sslContext;
	}

	public void setSslContext(SslContext sslContext) {
		this.sslContext = sslContext;
	}
	

}
