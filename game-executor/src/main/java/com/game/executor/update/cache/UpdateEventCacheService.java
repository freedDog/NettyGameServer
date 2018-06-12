package com.game.executor.update.cache;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.game.executor.common.utils.Loggers;
import com.game.executor.event.impl.event.UpdateEvent;

/**
 * update event 的缓存服务
 * @author JiangBangMing
 *
 * 2018年6月11日 下午12:50:59
 */
public class UpdateEventCacheService {

	public static UpdateEventCacheFactory updateEventCacheFactory;
	
	private static int size=1024;
	private static int maxSize=1024*32;
	
	private static boolean poolOpenFlag;
	
	public static void init() {
		
	}
	
	public static void start() {
		int size=getSize();
		int maxSize=getMaxSize();
		GenericObjectPoolConfig genericObjectPoolConfig=new GenericObjectPoolConfig();
		genericObjectPoolConfig.setMaxTotal(maxSize);
		genericObjectPoolConfig.setMaxIdle(maxSize);
		genericObjectPoolConfig.setMinIdle(size);
		long time=1000*30;
		genericObjectPoolConfig.setMaxWaitMillis(time);
		genericObjectPoolConfig.setSoftMinEvictableIdleTimeMillis(time);
		genericObjectPoolConfig.setTestOnReturn(true);
		
		updateEventCacheFactory=new UpdateEventCacheFactory(new UpdateEventPoolFactory(),genericObjectPoolConfig);
	}

	public static void stop() {
		if(updateEventCacheFactory!=null) {
			updateEventCacheFactory.close();
		}
	}
	
	public static UpdateEvent createUpdateEvent() {
		if(!poolOpenFlag) {
			UpdateEvent updateEvent=new UpdateEvent<>();
			return updateEvent;
		}
		try {
			return updateEventCacheFactory.borrowObject();
		}catch(Exception e) {
			Loggers.gameExecutorError.error(e.toString(),e);
		}
		return null;
	}
	
	public static void releaseUpdateEvent(UpdateEvent updateEvent) {
		if(!poolOpenFlag) {
			updateEvent=null;
			return;
		}
		updateEventCacheFactory.returnObject(updateEvent);
	}
	
	public static int getSize() {
		return size;
	}

	public static void setSize(int size) {
		UpdateEventCacheService.size = size;
	}

	public static int getMaxSize() {
		return maxSize;
	}

	public static void setMaxSize(int maxSize) {
		UpdateEventCacheService.maxSize = maxSize;
	}

	public static boolean isPoolOpenFlag() {
		return poolOpenFlag;
	}

	public static void setPoolOpenFlag(boolean poolOpenFlag) {
		UpdateEventCacheService.poolOpenFlag = poolOpenFlag;
	}
	
	
}
