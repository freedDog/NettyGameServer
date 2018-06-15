package com.game.service.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import com.game.bootstrap.manager.LocalMananger;
import com.game.common.annotation.GlobalEventListenerAnnotation;
import com.game.common.annotation.SpecialEventListenerAnnotation;
import com.game.common.config.GameServerConfig;
import com.game.common.constant.GlobalConstants;
import com.game.common.constant.Loggers;
import com.game.common.constant.ServiceName;
import com.game.common.scanner.ClassScanner;
import com.game.common.util.BeanUtil;
import com.game.common.util.StringUtils;
import com.game.executor.event.AbstractEventListener;
import com.game.executor.event.EventBus;
import com.game.executor.event.SingleEvent;
import com.game.executor.event.service.AsyncEventService;
import com.game.service.IService;
import com.game.service.classes.loader.DefaultClassLoader;
import com.game.service.classes.loader.DynamicGameClassLoader;
import com.game.service.config.GameServerConfigService;

/**
 * 游戏内的事件全局服务
 * @author JiangBangMing
 *
 * 2018年6月6日 下午5:35:05
 */
@Service
public class GameAsyncEventService implements IService{
	
	public static final Logger logger=Loggers.serverLogger;
	
	public ClassScanner classScanner=new ClassScanner();
	
	private AsyncEventService asyncEventService;
	
	private EventBus eventBus;
	/***
	 * 特殊事件监听器缓存
	 */
	private Map<Integer, AbstractEventListener> specialEventListenerMap;
	@Override
	public String getId() {
		return ServiceName.GameAsyncEventService;
	}

	@Override
	public void startup() throws Exception {
		eventBus=new EventBus();
		specialEventListenerMap=new ConcurrentHashMap<>();
		
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		GameServerConfig gameServerConfig=gameServerConfigService.getGameServerConfig();
		String nameSpace=gameServerConfig.getAsyncEventListenerNameSpace();
		scanListener(nameSpace, GlobalConstants.FileExtendConstants.Ext);
		int eventQueueSize=gameServerConfig.getAsyncEventQueueSize();
		int workerSize=gameServerConfig.getAsyncEventWorkSize();
		String queueWorkThreadName=GlobalConstants.Thread.ASYNC_EVENT_WORKER;
		int handlerSize=gameServerConfig.getAsyncEventHandlerThreadSize();
		String workerHandlerName=GlobalConstants.Thread.ASYNC_EVENT_HANDLER;
		int handlerQueueSize=gameServerConfig.getAsyncEventHandlerQueueSize();
		asyncEventService=new AsyncEventService(eventBus, eventQueueSize, workerSize, queueWorkThreadName, handlerSize, workerHandlerName, handlerQueueSize);
	}

	@Override
	public void shutdown() throws Exception {
		asyncEventService.shutDown();
		eventBus.clear();
	}
	
	/**
	 * 获取消息对象
	 * @param classes
	 * @return
	 */
	public final AbstractEventListener getListener(Class<?> classes) {
		try {
			if(null==classes) {
				return null;
			}
			//如果是spring 对象，直接获取，使用spring
			if(classes.getAnnotation(Service.class)!=null) {
				return (AbstractEventListener)BeanUtil.getBean(StringUtils.toLowerCaseFirstOne(classes.getSimpleName()));
			}
			
			AbstractEventListener object=(AbstractEventListener)classes.newInstance();
			return object;
			
		}catch(Exception e) {
			logger.error("getListener - classes="+classes.getName()+".",e);
		}
		return null;
	}
	/**
	 * 放入消息
	 * @param event
	 */
	public void putEvent(SingleEvent event) {
		asyncEventService.put(event);
	}
	/**
	 * 获取特殊条件事件类型
	 * @param eventType
	 * @return
	 */
	public AbstractEventListener getSpecialEventListener(int eventType) {
		return specialEventListenerMap.get(eventType);
	}
	private void scanListener(String namespace,String ext) throws Exception{
		String[] fileNames=this.classScanner.scannerPackage(namespace, ext);
		//加载class,获取协议命令
		DefaultClassLoader defaultClassLoader=LocalMananger.getInstance().getLocalSpringServiceManager().getDefaultClassLoader();
		defaultClassLoader.resetDynamicGameClassLoader();
		DynamicGameClassLoader dynamicGameClassLoader=defaultClassLoader.getDynamicGameClassLoader();
		if(fileNames!=null) {
			for(String fileName:fileNames) {
				String realClass=namespace+"."+fileName.substring(0,fileName.length()-(ext.length()));
				Class<?> messageClass=Class.forName(realClass);
				logger.info("GameAsyncEventService load:"+messageClass);
				
				AbstractEventListener eventListener=getListener(messageClass);
				GlobalEventListenerAnnotation annotation=(GlobalEventListenerAnnotation)messageClass.getAnnotation(GlobalEventListenerAnnotation.class);
				if(annotation!=null) {
					eventBus.addEventListener(eventListener);
				}
				//如果存在特殊监听器，放入特殊监听器
				SpecialEventListenerAnnotation specialEventListenerAnnotation=messageClass.getAnnotation(SpecialEventListenerAnnotation.class);
				if(specialEventListenerAnnotation!=null) {
					int speical=specialEventListenerAnnotation.listener();
					specialEventListenerMap.put(speical, eventListener);
				}
			}
		}
	}
}
