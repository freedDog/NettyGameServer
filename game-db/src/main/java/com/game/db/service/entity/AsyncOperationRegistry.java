package com.game.db.service.entity;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.game.common.scanner.ClassScanner;
import com.game.common.util.BeanUtil;
import com.game.db.common.DbServiceName;
import com.game.db.common.GlobalConstants;
import com.game.db.common.Loggers;
import com.game.db.common.annotation.AsyncEntityOperation;
import com.game.db.service.async.thread.AsyncDbOperation;
import com.game.db.service.common.service.IDbService;
import com.game.db.service.config.DbConfig;

/**
 * 异步服务注册中心
 * @author JiangBangMing
 *
 * 2018年6月21日 下午1:01:47
 */
@Service
public class AsyncOperationRegistry implements IDbService{
	
	private static Logger logger=Loggers.dbServerLogger;
	
	@Autowired
	private DbConfig dbConfig;

	@Autowired
	private BeanUtil beanUtil;
	
	/**
	 * 包体扫描
	 */
	public ClassScanner messageScanner=new ClassScanner();
	
	/**
	 *注册map
	 */
	private ConcurrentHashMap<String, AsyncDbOperation> opeartionMap=new ConcurrentHashMap<>();
	@Override
	public String getDbServiceName() {
		return DbServiceName.asyncOperationRegistry;
	}

	@Override
	public void startUp() throws Exception {
		loadPacke(dbConfig.getAsyncOperationPackageName(), GlobalConstants.ClassConstants.Ext);
	}

	@Override
	public void shutdown() throws Exception {
		
	}

	public void loadPacke(String namespace,String ext) throws Exception{
		String[] fileNames=messageScanner.scannerPackage(namespace, ext);
		//加载class,获取协议命令
		if(fileNames!=null) {
			for(String fileName:fileNames) {
				String realClass=namespace+"."+fileName.subSequence(0, fileName.length()-(ext.length()));
				Class<?> messageClass=Class.forName(realClass);
				AsyncEntityOperation asyncEntityOperation=messageClass.getAnnotation(AsyncEntityOperation.class);
				if(asyncEntityOperation!=null) {
					AsyncDbOperation asyncDbOperation=(AsyncDbOperation)beanUtil.getBean(asyncEntityOperation.bean());
					opeartionMap.put(messageClass.getSimpleName(), asyncDbOperation);
				}
			}
		}
	}
	
	public Collection<AsyncDbOperation> getAllAsyncEntityOperation(){
		return opeartionMap.values();
	}
	
	public DbConfig getDbConfig() {
		return dbConfig;
	}

	public void setDbConfig(DbConfig dbConfig) {
		this.dbConfig = dbConfig;
	}

	public BeanUtil getBeanUtil() {
		return beanUtil;
	}

	public void setBeanUtil(BeanUtil beanUtil) {
		this.beanUtil = beanUtil;
	}

	public ConcurrentHashMap<String, AsyncDbOperation> getOpeartionMap() {
		return opeartionMap;
	}

	public void setOpeartionMap(ConcurrentHashMap<String, AsyncDbOperation> opeartionMap) {
		this.opeartionMap = opeartionMap;
	}
	
}
