package com.game.service.net.websocket;

import java.net.URL;

import org.jdom2.Element;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.game.common.constant.GlobalConstants;
import com.game.common.constant.Loggers;
import com.game.common.util.FileUtil;
import com.game.common.util.JdomUtils;
import com.game.service.net.SdNetConfig;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月19日 下午8:58:31
 */
@Service
public class NetWebSocketServerConfig extends SdNetConfig{
	
	private static final Logger logger=Loggers.serverLogger;
	
	private SdWebSocketServerConfig sdWebSocketServerConfig;
	
	public void init() throws Exception{
		URL url=FileUtil.getConfigURL(GlobalConstants.ConfigFile.WEBSOCKET_SERVER_CONFIG);
		if(url!=null) {
			Element rootElement=JdomUtils.getRootElemet(url.getFile());
			Element element=rootElement.getChild("server");
			sdWebSocketServerConfig=new SdWebSocketServerConfig();
			sdWebSocketServerConfig.load(element);
		}
	}

	public SdWebSocketServerConfig getSdWebSocketServerConfig() {
		return sdWebSocketServerConfig;
	}

	public void setSdWebSocketServerConfig(SdWebSocketServerConfig sdWebSocketServerConfig) {
		this.sdWebSocketServerConfig = sdWebSocketServerConfig;
	}
	
	
}
