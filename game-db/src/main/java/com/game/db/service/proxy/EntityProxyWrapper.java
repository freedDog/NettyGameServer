package com.game.db.service.proxy;

import com.game.db.entity.AbstractEntity;

/**
 * 代理封装
 * @author JiangBangMing
 *
 * 2018年6月20日 下午12:40:41
 */
public class EntityProxyWrapper<T extends AbstractEntity> {

	private EntityProxy entityProxy;
	public EntityProxyWrapper(EntityProxy entityProxy) {
		this.entityProxy=entityProxy;
	}
	public EntityProxy getEntityProxy() {
		return entityProxy;
	}
	public void setEntityProxy(EntityProxy entityProxy) {
		this.entityProxy = entityProxy;
	}
	
	
}
