package com.game.bootstrap.manager;
/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月1日 下午3:43:00
 */

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;

import com.game.common.constant.Loggers;
import com.game.service.IService;

public abstract class AbstractLocalManager implements ILocalManager{
	
	protected static final Logger log=Loggers.serverLogger;
	
	protected Map<Class,Object> services;
	
	public AbstractLocalManager() {
		services=new LinkedHashMap<>();
	}

	@Override
	public <T> T get(Class<T> clazz) {
		return (T)services.get(clazz);
	}

	@Override
	public <X, Y extends X> void create(Class<Y> clazz, Class<X> inter) throws Exception {
		log.info(clazz.getSimpleName()+" is create");
		Object newObject=clazz.newInstance();
		if(newObject instanceof IService) {
			((IService)newObject).startup();
		}
		add(newObject,inter);
	}

	@Override
	public <T> void add(Object service, Class<T> inter) {
		log.info(service.getClass().getSimpleName()+" is add");
		services.put(inter, service);
	}

	@Override
	public void shutdown() {
		
		Object[] ss=services.values().toArray(new Object[0]);
		for(int i=ss.length-1;i>0;i--){
			if(ss[i] instanceof IService) {
				try {
					((IService)ss[i]).shutdown();
				}catch(Exception e) {
					Loggers.errorLogger.error(e.toString());
				}
			}
		}
	}
	
}
