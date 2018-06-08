package com.game.service.classes.loader;
/**
 * 动态游戏工厂
 * @author JiangBangMing
 *
 * 2018年6月1日 下午3:10:17
 */

import java.util.Hashtable;

import org.slf4j.Logger;

import com.game.common.constant.Loggers;

public class DynamicGameClassLoader extends ClassLoader {

	public static final Logger logger=Loggers.serverLogger;
	
	private Hashtable<String, Class> loadedClasses=new Hashtable<>();
	
	public Class<?> findClass(String className,byte[] b) throws ClassNotFoundException{
		logger.info("class loader find:"+className);
		Class<?> classes=defineClass(null, b, 0, b.length);
		loadedClasses.put(className, classes);
		return classes;
	}
	
	public synchronized Class loadClass(String className,boolean resolve) throws ClassNotFoundException{
		logger.info("class loader load:"+className);
		return super.loadClass(className,resolve);
	}
	protected Class<?> findClass(final String className) throws ClassNotFoundException{
		if(loadedClasses.containsKey(className)) {
			return loadedClasses.get(className);
		}
		return super.findClass(className);
	}
}
