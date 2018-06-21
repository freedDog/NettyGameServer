package com.game.db.service.async.transaction.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.game.common.util.ObjectUtils;
import com.game.db.common.Loggers;
import com.game.db.common.enums.DbOperationEnum;
import com.game.db.entity.AbstractEntity;
import com.game.db.service.async.AsyncEntityWrapper;
import com.game.db.service.async.thread.AsyncDbOperationMonitor;
import com.game.db.service.entity.EntityService;
import com.game.db.service.proxy.EntityProxyFactory;
import com.game.db.service.redis.RedisService;
import com.game.db.util.EntityUtils;
import com.redis.transaction.entity.AbstractGameTransactionEntity;
import com.redis.transaction.enums.GameTransactionCommitResult;
import com.redis.transaction.enums.GameTransactionEntityCause;
import com.redis.transaction.exception.GameTransactionException;
import com.redis.transaction.service.IRGTRedisService;

/**
 * 异步存储事务实体
 * @author JiangBangMing
 *
 * 2018年6月21日 下午7:27:17
 */
public class AsyncDBSaveTransactionEntity extends AbstractGameTransactionEntity{

	private Logger logger=Loggers.dbErrorLogger;
	
	/**
	 * db中redis服务
	 */
	private RedisService redisService;
	/**
	 * 实体存储服务
	 */
	private EntityService entityService;
	/**
	 * 需要弹出的玩家key
	 */
	private String playerKey;
	
	
	private EntityProxyFactory entityProxyFactory;
	
	private AsyncDbOperationMonitor asyncDbOperationMonitor;
	
	public AsyncDBSaveTransactionEntity(GameTransactionEntityCause cause,String playerKey,IRGTRedisService irgtRedisService
			,EntityService entityService,RedisService redisService,EntityProxyFactory entityProxyFactory) {
		super(cause,playerKey,irgtRedisService);
		this.playerKey=playerKey;
		this.entityService=entityService;
		this.redisService=redisService;
		this.entityProxyFactory=entityProxyFactory;
	}

	
	@Override
	public void commit() throws GameTransactionException {
		
	}


	@Override
	public void rollback() throws GameTransactionException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public GameTransactionCommitResult trycommit() throws GameTransactionException {
		// TODO Auto-generated method stub
		return null;
	}


	public AsyncDbOperationMonitor getAsyncDbOperationMonitor() {
		return asyncDbOperationMonitor;
	}

	public void setAsyncDbOperationMonitor(AsyncDbOperationMonitor asyncDbOperationMonitor) {
		this.asyncDbOperationMonitor = asyncDbOperationMonitor;
	}
	
	private void saveAsyncEntityWrapper(AsyncEntityWrapper asyncEntityWrapper) throws Exception{
		//开始进行反射，存储到mysql
		Class targeClass=entityService.getEntityTClass();
		DbOperationEnum dbOperationEnum=asyncEntityWrapper.getDbOperationEnum();
		if(dbOperationEnum.equals(DbOperationEnum.insert)) {
			AbstractEntity abstractEntity=ObjectUtils.getObjFromMap(asyncEntityWrapper.getParams(), targeClass);
			entityService.insertEntity(abstractEntity);
		}else if(dbOperationEnum.equals(DbOperationEnum.delete)) {
			AbstractEntity abstractEntity=ObjectUtils.getObjFromMap(asyncEntityWrapper.getParams(), targeClass);
			entityService.deleteEntity(abstractEntity);
			//TODO 进行回调删除
			EntityUtils.deleteEntity(redisService, abstractEntity);
		}else if(dbOperationEnum.equals(DbOperationEnum.update)) {
			AbstractEntity abstractEntity=(AbstractEntity)targeClass.newInstance();
			abstractEntity=entityProxyFactory.createProxyEntity(abstractEntity);
			Map<String, String> changeStrings=asyncEntityWrapper.getParams();
			ObjectUtils.getObjFromMap(changeStrings, abstractEntity);
			entityService.updateEntity(abstractEntity);
		}else if(dbOperationEnum.equals(DbOperationEnum.insertBatch)) {
			List<Map<String, String>> paramList=asyncEntityWrapper.getParamList();
			List<AbstractEntity> abstractEntities=new ArrayList<>();
			for(Map<String, String> temp:paramList) {
				AbstractEntity abstractEntity=ObjectUtils.getObjFromMap(asyncEntityWrapper.getParams(), targeClass);
				abstractEntities.add(abstractEntity);
			}
			entityService.insertEntityBatch(abstractEntities);
		}else if(dbOperationEnum.equals(DbOperationEnum.updateBatch)) {
			List<Map<String, String>> paramList=asyncEntityWrapper.getParamList();
			List<AbstractEntity> abstractEntities=new ArrayList<>();
			for(Map<String, String> temp:paramList) {
				AbstractEntity abstractEntity=(AbstractEntity)targeClass.newInstance();
				abstractEntity=entityProxyFactory.createProxyEntity(abstractEntity);
				abstractEntities.add(abstractEntity);
			}
			entityService.updateEntityBatch(abstractEntities);
		}else if(dbOperationEnum.equals(DbOperationEnum.deleteBatch)) {
			List<Map<String, String>> paramList=asyncEntityWrapper.getParamList();
			List<AbstractEntity> abstractEntities=new ArrayList<>();
			for(Map<String, String> temp:paramList) {
				AbstractEntity abstractEntity=ObjectUtils.getObjFromMap(asyncEntityWrapper.getParams(), targeClass);
				abstractEntities.add(abstractEntity);
			}
			entityService.deleteEntityBatch(abstractEntities);
		}
		asyncDbOperationMonitor.monitor();
	}
	
	
}
