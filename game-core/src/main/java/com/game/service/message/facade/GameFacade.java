package com.game.service.message.facade;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.annotation.MessageCommandAnnotation;
import com.game.common.constant.GlobalConstants;
import com.game.common.constant.Loggers;
import com.game.common.constant.ServiceName;
import com.game.common.exception.GameHandlerException;
import com.game.common.scanner.ClassScanner;
import com.game.message.handler.AbstractMessageHandler;
import com.game.message.handler.IMessageHandler;
import com.game.service.IService;
import com.game.service.Reloadable;
import com.game.service.classes.loader.DefaultClassLoader;
import com.game.service.classes.loader.DynamicGameClassLoader;
import com.game.service.config.GameServerConfigService;
import com.game.service.message.AbstractNetMessage;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月1日 下午12:24:05
 */
@Service
public class GameFacade implements IFacade,Reloadable,IService{
	
	public static final Logger logger=Loggers.serverLogger;
	
	public ClassScanner classScanner=new ClassScanner();
	
	public String[] fileNames;
	
	protected Map<Integer,IMessageHandler> handlers=new HashMap<>();
	
	public void addHandler(int httpCode,IMessageHandler handler) {
		this.handlers.put(httpCode, handler);
	}
	
	public AbstractNetMessage dispatch(AbstractNetMessage message) throws GameHandlerException {
		
		try {
			int cmd=message.getCmd();
			IMessageHandler handler=this.handlers.get(cmd);
			Method method=handler.getMessageHandler(cmd);
			method.setAccessible(true);
			Object object=method.invoke(handler, message);
			AbstractNetMessage result=null;
			if(object!=null) {
				result=(AbstractNetMessage)object;
			}
			return result;
		}catch(Exception e) {
			throw new GameHandlerException(e,message.getSerial());
		}
	}
	
	public void loadPackage(String namespace,String ext) throws Exception{
		if(null==fileNames) {
			fileNames=this.classScanner.scannerPackage(namespace, ext);
		}
		//加载class,获取协议命令
		DefaultClassLoader defaultClassLoader=LocalMananger.getInstance().getLocalSpringServiceManager().getDefaultClassLoader();
		defaultClassLoader.resetDynamicGameClassLoader();
		DynamicGameClassLoader dynamicGameClassLoader=defaultClassLoader.getDynamicGameClassLoader();
		
		if(fileNames!=null) {
			for(String fileName:fileNames) {
				String realClass=namespace+"."+fileName.substring(0,fileName.length()-(ext.length()));
				Class<?> messageClass=Class.forName(realClass);
				logger.info("handler load:"+messageClass);
				
				IMessageHandler iMessageHandler=getMessageHandler(messageClass);
				AbstractMessageHandler handler=(AbstractMessageHandler)iMessageHandler;
				handler.init();
				Method[] methods=messageClass.getMethods();
				for(Method method:methods) {
					if(method.isAnnotationPresent(MessageCommandAnnotation.class)) {
						MessageCommandAnnotation messageCommandAnnotation=(MessageCommandAnnotation)method.getAnnotation(MessageCommandAnnotation.class);
						if(messageCommandAnnotation!=null) {
							addHandler(messageCommandAnnotation.command(), iMessageHandler);
						}
					}
				}
			}
		}
	}
	/**
	 * 获取消息对象
	 * @param classes
	 * @return
	 */
	public final IMessageHandler getMessageHandler(Class<?> classes) {
		try {
			if(null==classes) {
				return null;
			}
			IMessageHandler messageHandler=(IMessageHandler)classes.newInstance();
			return messageHandler;
		}catch(Exception e) {
			logger.error("getMessageHandler - classes="+classes.getName()+".",e);
		}
		return null;
	}
	
	public String getId() {
		return ServiceName.GameFacade;
	}

	public void startup() throws Exception {
		this.reload();
	}

	public void shutdown() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reload() throws Exception {

		try {
			GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
			loadPackage(gameServerConfigService.getGameServerConfig().getNetMessageHandlerNameSpace(), GlobalConstants.FileExtendConstants.Ext);
		}catch(Exception e) {
			logger.error(e.toString(),e);
		}
	}

	

}
