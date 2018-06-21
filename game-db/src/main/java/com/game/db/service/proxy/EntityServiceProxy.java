package com.game.db.service.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import com.game.common.util.ObjectUtils;
import com.game.db.common.Loggers;
import com.game.db.common.annotation.DbOperation;
import com.game.db.common.enums.DbOperationEnum;
import com.game.db.entity.AbstractEntity;
import com.game.db.entity.IEntity;
import com.game.db.service.entity.EntityService;
import com.game.db.service.redis.RedisInterface;
import com.game.db.service.redis.RedisListInterface;
import com.game.db.service.redis.RedisService;
import com.game.db.util.EntityUtils;

/**
 * 实体存储服务代理 同步存储
 * 
 * 存储策略为 insert的时候插入db,然后更新缓存。query的时候优先缓存，找不到的时候查询db，更新缓存。
 * delete的时候删除db,删除缓存
 * useRedisFlag为是否使用缓存redis标志
 * @author JiangBangMing
 *
 * 2018年6月21日 下午5:08:40
 */
public class EntityServiceProxy<T extends EntityService> implements MethodInterceptor{
	
	private static final Logger logger=Loggers.dbServiceProxyLogger;
	
	private RedisService redisService;
	
	private boolean useRedisFlag;
	
	public EntityServiceProxy(RedisService redisService,boolean useRedisFlag) {
		this.redisService=redisService;
		this.useRedisFlag=useRedisFlag;
	}
	
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		Object result=null;
		DbOperation dbOperation=method.getAnnotation(DbOperation.class);
		if(null==dbOperation||!useRedisFlag) {
			//如果咩有进行注解或者不使用redis,直接进行返回
			result=methodProxy.invoke(obj, args);
		}else {
			//进行数据库操作
			DbOperationEnum dbOperationEnum=dbOperation.operation();
			switch (dbOperationEnum) {
			case insert:
				result=methodProxy.invokeSuper(obj, args);
				AbstractEntity abstractEntity=(AbstractEntity)args[0];
				EntityUtils.updateAllFieldEntity(redisService, abstractEntity);
				break;
			case update:
				result=methodProxy.invokeSuper(obj, args);
				abstractEntity=(AbstractEntity)args[0];
				EntityUtils.updateChangedFieldEntity(redisService, abstractEntity);
				break;
			case query:
				abstractEntity=(AbstractEntity)args[0];
				if(abstractEntity!=null) {
					if(abstractEntity instanceof RedisInterface) {
						RedisInterface redisInterface=(RedisInterface)abstractEntity;
						result=redisService.getObjectFromHash(EntityUtils.getRedisKey(redisInterface), abstractEntity.getClass());
					}else {
						logger.error("query interface RedisListInterface "+abstractEntity.getClass().getSimpleName()+" use RedisInterface "+abstractEntity.toString());
					}
				}
				if(result==null) {
					result=methodProxy.invokeSuper(obj, args);
					if(result!=null) {
						abstractEntity=(AbstractEntity)result;
						EntityUtils.updateAllFieldEntity(redisService, abstractEntity);
					}
				}
				break;
			case queryList:
				abstractEntity=(AbstractEntity)args[0];
				if(abstractEntity!=null) {
					if(abstractEntity instanceof RedisListInterface) {
						RedisListInterface redisListInterface=(RedisListInterface)abstractEntity;
						result=redisService.getListFromHash(EntityUtils.getRedisKeyByRedisListInterface(redisListInterface),abstractEntity.getClass());
						if(result!=null) {
							result=filterEntity((List<IEntity>)result, abstractEntity);
						}
					}else {
						logger.error("query interface RedisListInterface "+abstractEntity.getClass().getSimpleName()+" use RedisInterface "+abstractEntity.toString());
					}
				}
				if(result==null) {
					result=methodProxy.invokeSuper(obj, args);
					if(result!=null) {
						List<AbstractEntity> entityList=(List<AbstractEntity>)result;
						EntityUtils.updateAllFieldEntityList(redisService, entityList);
					}
				}
				break;
			case delete:
				result=methodProxy.invokeSuper(obj, args);
				abstractEntity=(AbstractEntity)args[0];
				EntityUtils.deleteEntity(redisService, abstractEntity);
				break;
			case insertBatch:
				result=methodProxy.invokeSuper(obj, args);
				List<AbstractEntity> entityList=(List<AbstractEntity>)args[0];
				EntityUtils.updateAllFieldEntityList(redisService, entityList);
				break;
			case updateBatch:
				result=methodProxy.invokeSuper(obj, args);
				entityList=(List<AbstractEntity>)args[0];
				EntityUtils.updateChangedFieldEntityList(redisService, entityList);
				break;
			case deleteBatch:
				result=methodProxy.invokeSuper(obj, args);
				entityList=(List<AbstractEntity>)args[0];
				EntityUtils.deleteEntityList(redisService, entityList);
				break;
			}
			
		}
		return result;
	}

	/**
	 * 根据封装的条件判断查找出相同的对象
	 * @param list
	 * @param abstractEntity
	 * @return
	 */
	public List<IEntity> filterEntity(List<IEntity> list,AbstractEntity abstractEntity){
		List<IEntity> result=new ArrayList<>();
		//开始进行filter
		EntityProxyWrapper entityProxyWrapper=abstractEntity.getEntityProxyWrapper();
		if(entityProxyWrapper!=null) {
			Map<String, Object> changeParamSet=entityProxyWrapper.getEntityProxy().getChangeParamSet();
			if(changeParamSet!=null) {
				for(IEntity entity:list) {
					boolean equalFlag=false;
					for(String fieldName:changeParamSet.keySet()) {
						String value=ObjectUtils.getFieldsValueStr(entity, fieldName);
						
						Object object=changeParamSet.get(fieldName);
						if(value.equals(object.toString())) {
							equalFlag=true;
						}else {
							equalFlag=false;
							break;
						}
					}
					if(equalFlag) {
						result.add(entity);
					}
						
				}
			}
		}
		return result;
	}
}
