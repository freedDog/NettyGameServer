package com.game.service.net.proxy;


import java.net.URL;

import org.jdom2.Element;
import org.slf4j.Logger;

import com.game.common.constant.GlobalConstants;
import com.game.common.constant.Loggers;
import com.game.common.util.FileUtil;
import com.game.common.util.JdomUtils;

/**
 * 记载代理
 * @author JiangBangMing
 *
 * 2018年6月19日 下午7:35:25
 */
public class NetProxyConfig {

		private static final Logger logger=Loggers.serverLogger;
		
		private SdProxyConfig sdProxyConfig;
		
		public void init() throws Exception{
			URL url=FileUtil.getConfigURL(GlobalConstants.ConfigFile.PROXY_SERVER_CONFIG);
			if(url!=null) {
				Element rootElement=JdomUtils.getRootElemet(url.getFile());
				Element element=rootElement.getChild("server");
				sdProxyConfig=new SdProxyConfig();
				sdProxyConfig.load(element);
			}
		}

		public SdProxyConfig getSdProxyConfig() {
			return sdProxyConfig;
		}

		public void setSdProxyConfig(SdProxyConfig sdProxyConfig) {
			this.sdProxyConfig = sdProxyConfig;
		}
		
}
