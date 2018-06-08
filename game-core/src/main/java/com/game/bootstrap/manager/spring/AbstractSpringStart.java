package com.game.bootstrap.manager.spring;
/**
 * 抽象的spring 启动
 * @author JiangBangMing
 *
 * 2018年5月31日 下午7:44:38
 */

import java.lang.reflect.Field;
import org.slf4j.Logger;
import com.game.common.constant.Loggers;
import com.game.service.IService;

public abstract class AbstractSpringStart {
	private Logger logger=Loggers.serverLogger;
	
	public void start() throws Exception{
		//获取对象obJ的所有属性域
		Field[] fields=this.getClass().getDeclaredFields();
		
		for(Field field:fields) {
			//对于每个属性，获取属性名
			String varName=field.getName();
			try {
				boolean access=field.isAccessible();
				if(!access) {
					field.setAccessible(true);
				}
				
				//从obj中获取field变量
				Object object=field.get(this);
				if(object instanceof IService) {
					IService iService=(IService)object;
					iService.startup();
					logger.info(iService.getId()+" service start up");
				}else {
					logger.info(object.getClass().getSimpleName()+" start up");
				}
				if(!access) {
					field.setAccessible(false);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop() throws Exception{
		//获取对象 obj 的所有属性
		Field[] fields=this.getClass().getDeclaredFields();
		for(Field field:fields) {
			//对于每个属性，获取属性名
			String varName=field.getName();
			try {
				boolean access=field.isAccessible();
				if(!access) {
					field.setAccessible(true);
				}
				//从obj中获取field变量
				Object object=field.get(this);
				if(object instanceof IService) {
					IService iService=(IService)object;
					iService.shutdown();
					logger.info(iService.getId()+" shut down");
				}else {
					logger.info(object.getClass().getSimpleName()+" shut down");
				}
				if(!access) {
					field.setAccessible(false);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
