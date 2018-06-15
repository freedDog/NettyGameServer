package com.game.service.net.udp;

import java.net.URL;

import org.jdom2.Element;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.game.common.constant.GlobalConstants;
import com.game.common.constant.Loggers;
import com.game.common.util.FileUtil;
import com.game.common.util.JdomUtils;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月14日 上午10:53:26
 */
@Service
public class NetUdpServerConfig {

	private static final Logger logger=Loggers.serverLogger;
	
	private SdUdpServerConfig sdUdpServerConfig;
	
	public void init() throws Exception{
		URL url=FileUtil.getConfigURL(GlobalConstants.ConfigFile.UDP_SERVER_CONFIG);
		if(url!=null) {
			Element rootElement=JdomUtils.getRootElemet(url.getFile());
			Element element=rootElement.getChild("server");
			sdUdpServerConfig=new SdUdpServerConfig();
			sdUdpServerConfig.load(element);
		}
	}

	public SdUdpServerConfig getSdUdpServerConfig() {
		return sdUdpServerConfig;
	}

	public void setSdUdpServerConfig(SdUdpServerConfig sdUdpServerConfig) {
		this.sdUdpServerConfig = sdUdpServerConfig;
	}
	
	
}
