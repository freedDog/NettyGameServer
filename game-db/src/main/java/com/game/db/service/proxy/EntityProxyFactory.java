package com.game.db.service.proxy;

import org.springframework.beans.BeanUtils;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.stereotype.Service;

import com.game.db.entity.AbstractEntity;
import com.game.db.entity.IEntity;

/**
 * 实体代理服务
 * @author JiangBangMing
 *
 * 2018年6月21日 下午4:47:01
 */
@Service
public class EntityProxyFactory {

	
	public <T extends IEntity> T createProxyEntity(T entity) throws Exception{
		EntityProxy entityProxy=createProxy(entity);
		EntityProxyWrapper entityProxyWrapper=new EntityProxyWrapper(entityProxy);
		AbstractEntity proxyEntity=createProxyEntity(entityProxy);
		//注入对象 数值
		BeanUtils.copyProperties(proxyEntity, entity);
		entityProxy.setCollectFlag(true);
		proxyEntity.setEntityProxyWrapper(entityProxyWrapper);
		return (T)proxyEntity;
	}
	
	private <T extends IEntity> T createProxyEntity(EntityProxy entityProxy) {
		Enhancer enhancer=new Enhancer();
		//设置需要创建子类的类
		enhancer.setSuperclass(entityProxy.getEntity().getClass());
		enhancer.setCallback(entityProxy);
		//通过字节码技术动态创建子类实例
		return (T)enhancer.create();
	}
	
	private EntityProxy createProxy(IEntity entity) {
		return new EntityProxy(entity);
	}
}
