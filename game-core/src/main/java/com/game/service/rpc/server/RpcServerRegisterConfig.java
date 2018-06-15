package com.game.service.rpc.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.game.common.constant.GlobalConstants;
import com.game.common.constant.Loggers;
import com.game.common.enums.BOEnum;
import com.game.common.util.FileUtil;
import com.game.common.util.JdomUtils;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月12日 下午7:12:22
 */

@Service
public class RpcServerRegisterConfig {
	
	private static final Logger logger=Loggers.rpcLogger;
	
	protected Object lock=new Object();
	
	protected List<SdServer> sdWorldServers;
	protected List<SdServer> sdGameServers;
	protected List<SdServer> sdDbServers;
	
	private SdRpcServiceProvider sdRpcServiceProvider;
	
	@SuppressWarnings("unused")
	public void init() throws Exception{
		Element rootElement=JdomUtils.getRootElemet(FileUtil.getConfigURL(GlobalConstants.ConfigFile.RPC_SERVER_REGISTER_CONFIG).getFile());
		
		Map<Integer, SdServer> serverMap=new HashMap<>();
		
		List<SdServer> sdWorldServers=new ArrayList<>();
		Element element=rootElement.getChild(BOEnum.WORLD.toString().toLowerCase());
		List<Element> childrenElements=element.getChildren("server");
		for(Element childElement:childrenElements) {
			SdServer sdServer=new SdServer();
			sdServer.load(childElement);
			sdWorldServers.add(sdServer);
		}
		
		List<SdServer> sdGameServers=new ArrayList<>();
		element=rootElement.getChild(BOEnum.GAME.toString().toLowerCase());
		childrenElements=element.getChildren("server");
		for(Element childElement:childrenElements) {
			SdServer sdServer=new SdServer();
			sdServer.load(childElement);
			sdGameServers.add(sdServer);
		}
		
		List<SdServer> sdDbServers=new ArrayList<>();
		element=rootElement.getChild(BOEnum.DB.toString().toLowerCase());
		childrenElements=element.getChildren("server");
		for(Element childElement:childrenElements) {
			SdServer sdServer=new SdServer();
			sdServer.load(childElement);
			sdDbServers.add(sdServer);
		}
		
		synchronized (this.lock) {
			this.sdWorldServers=sdWorldServers;
			this.sdGameServers=sdGameServers;
			this.sdDbServers=sdDbServers;
		}
		SdRpcServiceProvider sdRpcServiceProvider=new SdRpcServiceProvider();
		rootElement=JdomUtils.getRootElemet(FileUtil.getConfigURL(GlobalConstants.ConfigFile.RPC_SERVEICE_CONFIG).getFile());
		childrenElements=rootElement.getChildren("service");
		for(Element childElement:childrenElements) {
			sdRpcServiceProvider.load(childElement);
		}
		
		synchronized (this.lock) {
			this.sdRpcServiceProvider=sdRpcServiceProvider;
		}
	}

	public boolean validServer(int boId) {
		return sdRpcServiceProvider.validSrver(boId);
	}
	public List<SdServer> getSdWorldServers() {
		return sdWorldServers;
	}

	public void setSdWorldServers(List<SdServer> sdWorldServers) {
		this.sdWorldServers = sdWorldServers;
	}

	public List<SdServer> getSdGameServers() {
		return sdGameServers;
	}

	public void setSdGameServers(List<SdServer> sdGameServers) {
		this.sdGameServers = sdGameServers;
	}

	public List<SdServer> getSdDbServers() {
		return sdDbServers;
	}

	public void setSdDbServers(List<SdServer> sdDbServers) {
		this.sdDbServers = sdDbServers;
	}

	public SdRpcServiceProvider getSdRpcServiceProvider() {
		return sdRpcServiceProvider;
	}

	public void setSdRpcServiceProvider(SdRpcServiceProvider sdRpcServiceProvider) {
		this.sdRpcServiceProvider = sdRpcServiceProvider;
	}
	

}
