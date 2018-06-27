package com.game.service.rpc.server;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.annotation.RpcServiceAnnotation;
import com.game.common.constant.GlobalConstants;
import com.game.common.constant.Loggers;
import com.game.common.constant.ServiceName;
import com.game.common.scanner.ClassScanner;
import com.game.service.IService;
import com.game.service.IReloadable;
import com.game.service.config.GameServerConfigService;
import com.game.service.rpc.serialize.protostuff.ProtostuffSerializeI;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月12日 下午7:32:04
 */
@Service
public class RpcMethodRegistry implements IReloadable,IService{
	
	public static Logger logger=Loggers.serverLogger;
	
	public ClassScanner classScanner=new ClassScanner();
	
	private ConcurrentHashMap<String, Object> registryMap=new ConcurrentHashMap<>();

	@Override
	public String getId() {
		return ServiceName.RpcMethodRegistry;
	}

	@Override
	public void startup() throws Exception {
		this.reload();
	}

	@Override
	public void shutdown() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reload() throws Exception {
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		String packegeName=gameServerConfigService.getGameServerConfig().getRpcServicePackage();
		loadPackage(packegeName, GlobalConstants.FileExtendConstants.Ext);
	}
	
	public void loadPackage(String namespace,String ext) throws Exception{
		String[] fileNames=classScanner.scannerPackage(namespace, ext);
		//加载class,获取协议命令
		if(fileNames!=null) {
			for(String fileName:fileNames) {
				String realClass=namespace+"."
						+fileName.subSequence(0, fileName.length()-(ext.length()));
				Class<?> messageClass=Class.forName(realClass);
				
				logger.info("rpc load:"+messageClass);
				RpcServiceAnnotation rpcServiceAnnotation=messageClass.getAnnotation(RpcServiceAnnotation.class);
				if(rpcServiceAnnotation!=null) {
					String interfaceName=messageClass.getAnnotation(RpcServiceAnnotation.class).value().getName();
					ProtostuffSerializeI rpcSerialize=LocalMananger.getInstance().getLocalSpringBeanManager().getProtostuffSerialize();
					Object serviceBean=(Object)rpcSerialize.newInstance(messageClass);
					registryMap.put(interfaceName, serviceBean);
					logger.info("rpc register:"+messageClass);
				}
			}
		}
	}
	public Object getServiceBean(String className) {
		return this.registryMap.get(className);
	}
}
