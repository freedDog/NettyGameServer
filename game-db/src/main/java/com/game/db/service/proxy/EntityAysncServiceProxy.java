package com.game.db.service.proxy;

import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import com.game.db.common.Loggers;
import com.game.db.common.annotation.DbOperation;
import com.game.db.common.enums.DbOperationEnum;
import com.game.db.entity.AbstractEntity;
import com.game.db.entity.IEntity;
import com.game.db.service.async.AsyncDbRegisterCenter;
import com.game.db.service.entity.EntityService;
import com.game.db.service.redis.AsyncSave;
import com.game.db.service.redis.RedisInterface;
import com.game.db.service.redis.RedisListInterface;
import com.game.db.service.redis.RedisService;
import com.game.db.util.EntityUtils;

/**
 * 存储策略为全部存入缓存(包括删除)，然后存入队列，进行异步线程存入db
 * @author JiangBangMing
 *
 * 2018年6月21日 下午5:49:10
 */
public class EntityAysncServiceProxy <T extends EntityService> extends EntityServiceProxy implements MethodInterceptor{
	
	private static final Logger logger=Loggers.dbServiceProxyLogger;
	
	private RedisService redisService;
	
	private AsyncDbRegisterCenter asyncDbRegisterCenter;
	
	public EntityAysncServiceProxy(RedisService redisService,AsyncDbRegisterCenter asyncDbRegisterCenter) {
		super(redisService,true);
		this.redisService=redisService;
		this.asyncDbRegisterCenter=asyncDbRegisterCenter;
	}

	
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		Object result=null;
		DbOperation dbOperation=method.getAnnotation(DbOperation.class);
		if(dbOperation==null) {
			//如果没有进行注解，直接进行返回
			result=methodProxy.invokeSuper(obj, args);
		}else {
			//进行数据库操作
			DbOperationEnum dbOperationEnum=dbOperation.operation();
			switch (dbOperationEnum) {
			case insert:
				AbstractEntity abstractEntity=(AbstractEntity)args[0];
				EntityUtils.updateAllFieldEntity(redisService, abstractEntity);
				asyncSaveEntity((EntityService)obj, dbOperationEnum, abstractEntity);
				break;
			case insertBatch:
				List<AbstractEntity> entities=(List<AbstractEntity>)args[0];
				EntityUtils.updateAllFieldEntityList(redisService, entities);
				asyncBatchSaveEntity((EntityService)obj, dbOperationEnum, entities);
				break;
			case update:
				abstractEntity=(AbstractEntity)args[0];
				EntityUtils.updateChangedFieldEntity(redisService, abstractEntity);
				asyncSaveEntity((EntityService)obj, dbOperationEnum, abstractEntity);
				break;
			case updateBatch:
				entities=(List<AbstractEntity>)args[0];
				EntityUtils.updateChangedFieldEntityList(redisService, entities);
				asyncBatchSaveEntity((EntityService)obj, dbOperationEnum, entities);
				break;
			case delete:
				abstractEntity=(AbstractEntity)args[0];
				//如果是删除，db删除后执行回调
				asyncSaveEntity((EntityService)obj,dbOperationEnum, abstractEntity);
				break;
			case deleteBatch:
				entities=(List<AbstractEntity>)args[0];
				EntityUtils.deleteEntityList(redisService, entities);
				asyncBatchSaveEntity((EntityService)obj, dbOperationEnum, entities);
				break;
			case query:
				abstractEntity=(AbstractEntity)args[0];
				if(abstractEntity!=null) {
					if(abstractEntity instanceof RedisInterface) {
						RedisInterface redisInterface=(RedisInterface)abstractEntity;
						result=redisService.getObjectFromHash(EntityUtils.getRedisKey(redisInterface),abstractEntity.getClass());
					}else {
						 logger.error("query interface RedisListInterface " + abstractEntity.getClass().getSimpleName() + " use RedisInterface " + abstractEntity.toString());
					}
				}
				break;
			case queryList:
				abstractEntity=(AbstractEntity)args[0];
				if(abstractEntity!=null) {
					if(abstractEntity instanceof RedisListInterface) {
						RedisListInterface redisListInterface=(RedisListInterface)abstractEntity;
						result=redisService.getListFromHash(EntityUtils.getRedisKeyByRedisListInterface(redisListInterface), abstractEntity.getClass());
						if(result!=null) {
							result=filterEntity((List<IEntity>)result, abstractEntity);
						}
					}else {
						logger.error("query interface RedisListInterface " + abstractEntity.getClass().getSimpleName() + " use RedisInterface " + abstractEntity.toString());
					}
				}
				break;
			default:
				break;
			}
		}
		
		return result;
	}


	/**
	 * 个体放入异步注册中心
	 * @param entityService
	 * @param dpDbOperationEnum
	 * @param abstractEntity
	 */
	public void asyncSaveEntity(EntityService entityService,DbOperationEnum dpDbOperationEnum,AbstractEntity abstractEntity) {
		//放入异步存储的key
		if(abstractEntity instanceof AsyncSave) {
			asyncDbRegisterCenter.asyncRegisterEntity(entityService, dpDbOperationEnum, abstractEntity);
		}else {
			logger.error("async save interface not asynccachekey " + abstractEntity.getClass().getSimpleName() + " use " + abstractEntity.toString());
		}
	}
	
	/**
	 * 批量放入异步注册中心
	 * @param entityService
	 * @param dbOperationEnum
	 * @param abstractEntities
	 */
	public void asyncBatchSaveEntity(EntityService entityService,DbOperationEnum dbOperationEnum,List<AbstractEntity> abstractEntities) {
		if(abstractEntities.size()>0) {
			AbstractEntity abstractEntity=abstractEntities.get(0);
			//放入异步存储的key
			if(abstractEntity instanceof AsyncSave) {
				asyncDbRegisterCenter.asyncBatchRegisterEntity(entityService, dbOperationEnum, abstractEntities);
			}else {
				logger.error("async batch save interface not asynccachekey " + abstractEntity.getClass().getSimpleName() + " use " + abstractEntity.toString());
			}
		}
	}
}
