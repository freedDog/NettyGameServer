package com.game.common.config;
/**
 * 获取properties 配置文件
 * @author JiangBangMing
 *
 * 2018年6月4日 下午4:12:34
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.springframework.core.io.Resource;

import com.game.common.constant.Loggers;

public abstract class AbstractGameConfigure implements GameConfigurable{

	protected static Logger logger=Loggers.adminLogger;
	private Properties properties;
	private File configFile;
	private Resource resource;
	private Object lock=new Object();
	
	public void setResource(Resource resource) {
		this.resource=resource;
	}
	
	public void init() {
		try {
			configFile=resource.getFile();
			properties=new Properties();
			properties.load(new FileInputStream(configFile));
			logger.info("加载配置文件："+resource.getFilename()+" 成功.");
		}catch (IOException e) {
			logger.error("加载配置文件："+resource.getFilename()+" 失败.");
		}
	}
	
	public void reload() {
		synchronized (lock) {
			try {
				properties.load(new FileInputStream(configFile));
				logger.info("relaod配置文件:"+resource.getFilename()+" 成功.");
			}catch (Exception e) {
				logger.error("reload 配置文件："+resource.getFilename()+" 失败.");
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getProperty(String key, String defaultVal) {
		String v=getProperty(key);
		if(null==v) {
			return defaultVal;
		}
		return v;
	}	

	@Override
	public int getProperty(String key, int defaultVal) {
		String v=getProperty(key);
		if(null==v) {
			return defaultVal;
		}
		Integer i=Integer.parseInt(v);
		return i.intValue();
	}

	@Override
	public boolean getProperty(String key, boolean defaultVal) {
		String v=getProperty(key);
		if(null==v) {
			return defaultVal;
		}
		boolean result=Boolean.parseBoolean(v);
		return result;
	}

	@Override
	public long getProperty(String key, long defaultVal) {
		String v=getProperty(key);
		if(null==v) {
			return defaultVal;
		}
		Long i=Long.parseLong(v);
		return i.longValue();
	}
	public String getProperty(String key) {
		if(null==key) {
			return null;
		}
		String v=properties.getProperty(key);
		if(null==v||v.isEmpty()) {
			return null;
		}
		return v.trim();
	}
}
