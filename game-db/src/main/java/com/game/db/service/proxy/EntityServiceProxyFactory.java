package com.game.db.service.proxy;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;

import com.game.db.service.entity.EntityService;
import com.game.db.service.redis.RedisService;

/**
 * 实体存储服务代理服务
 * @author JiangBangMing
 *
 * 2018年6月21日 下午7:10:13
 */
public class EntityServiceProxyFactory {

	@Autowired
	private RedisService redisService;
	
	@Autowired(required=false)
	private boolean useRedisFlag=true;
	
	public <T extends EntityService> T createProxyService(T entityService) throws Exception{
		T proxyEntityService=(T)createProxyService(entityService,createProxy(entityService));
		BeanUtils.copyProperties(proxyEntityService, entityService);
		return proxyEntityService;
	}
	
	
	public RedisService getRedisService() {
		return redisService;
	}

	public void setRedisService(RedisService redisService) {
		this.redisService = redisService;
	}

	private EntityServiceProxy createProxy(EntityService entityService) {
		return new EntityServiceProxy<>(redisService, useRedisFlag);
	}
	
	private <T extends EntityService> T createProxyService(T entityService,EntityServiceProxy entityServiceProxy) {
		Enhancer enhancer=new Enhancer();
		//设置需要创建的子类的类
		enhancer.setSuperclass(entityService.getClass());
		enhancer.setCallback(entityServiceProxy);
		//通过字节码技术动态创建子类实例
		return (T)enhancer.create();
	}
}
