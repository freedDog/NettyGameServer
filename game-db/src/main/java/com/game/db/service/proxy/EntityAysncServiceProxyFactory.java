package com.game.db.service.proxy;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;

import com.game.db.service.async.AsyncDbRegisterCenter;
import com.game.db.service.entity.EntityService;
import com.game.db.service.redis.RedisService;

/**
 * 实体存储异步代理服务工厂
 * @author JiangBangMing
 *
 * 2018年6月21日 下午7:18:00
 */
public class EntityAysncServiceProxyFactory {

	@Autowired
	private RedisService redisService;
	@Autowired
	private AsyncDbRegisterCenter asyncDbRegisterCenter;
	
	
	public <T extends EntityService> T createProxyService(T entityService) throws Exception{
		T proxyEntityService=(T)createProxyService(entityService,createProxy(entityService,asyncDbRegisterCenter));
		BeanUtils.copyProperties(proxyEntityService, entityService);
		return proxyEntityService;
	}
	
	private EntityAysncServiceProxy createProxy(EntityService entityService,AsyncDbRegisterCenter asyncDbRegisterCenter) {
		return new EntityAysncServiceProxy<>(redisService, asyncDbRegisterCenter);
	}
	
	private <T extends EntityService> T createProxyService(T entitySrvice,EntityAysncServiceProxy entityAysncServiceProxy) {
		Enhancer enhancer=new Enhancer();
		//设置需要创建子类的类
		enhancer.setSuperclass(entitySrvice.getClass());
		enhancer.setCallback(entityAysncServiceProxy);
		//通过字节码动态创建子类实例
		return (T)enhancer.create();
	}
}
