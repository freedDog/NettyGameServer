package com.game.executor.update.cache;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.game.executor.event.impl.event.UpdateEvent;

/**
 * update event 因为使用太频繁，使用 commonpool2 缓存
 * @author JiangBangMing
 *
 * 2018年6月11日 下午12:51:31
 */
public class UpdateEventCacheFactory extends GenericObjectPool<UpdateEvent>{

	public UpdateEventCacheFactory(PooledObjectFactory<UpdateEvent> factory,GenericObjectPoolConfig config) {
		super(factory,config);
	}

}
