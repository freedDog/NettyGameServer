package com.game.service.net.http;

import java.net.URL;

import org.jdom2.Element;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.game.common.constant.GlobalConstants;
import com.game.common.constant.Loggers;
import com.game.common.util.FileUtil;
import com.game.common.util.JdomUtils;

/**
 * http 服务器配置
 * @author JiangBangMing
 *
 * 2018年6月14日 下午1:42:43
 */
@Service
public class NetHttpServerConfig {

	private static final Logger logger=Loggers.serverLogger;
	
	private SdHttpServerConfig sdHttpServerConfig;
	
	public void init() throws Exception{
		URL url=FileUtil.getConfigURL(GlobalConstants.ConfigFile.HTTP_SERVER_CONFIG);
		if(url!=null) {
			Element rootElement=JdomUtils.getRootElemet(url.getFile());
			Element element=rootElement.getChild("server");
			sdHttpServerConfig=new SdHttpServerConfig();
			sdHttpServerConfig.load(element);
		}
	}

	public SdHttpServerConfig getSdHttpServerConfig() {
		return sdHttpServerConfig;
	}

	public void setSdHttpServerConfig(SdHttpServerConfig sdHttpServerConfig) {
		this.sdHttpServerConfig = sdHttpServerConfig;
	}
	
	
}
