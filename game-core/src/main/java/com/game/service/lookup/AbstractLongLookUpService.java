package com.game.service.lookup;
/**
 * 抽象long 型id 查找内容
 * @author JiangBangMing
 *
 * 2018年6月4日 上午11:00:49
 */

import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import com.game.common.constant.Loggers;

public abstract class AbstractLongLookUpService<T extends ILongId> implements ILongLookupService<T>{

	protected static final Logger logger=Loggers.serverStatusStatistics;
	
	protected ConcurrentHashMap<Long, T> tMap=new ConcurrentHashMap<>();

	@Override
	public T lookup(long id) {
		return tMap.get(id);
	}

	@Override
	public void addT(T t) {
		if(logger.isDebugEnabled()) {
			logger.debug("add T "+t.getClass().getSimpleName()+" id"+t.longId());
		}
		tMap.put(t.longId(), t);
	}

	@Override
	public boolean removeT(T t) {
		if(logger.isDebugEnabled()) {
			logger.debug("remove t "+t.getClass().getSimpleName()+" id "+t.longId());
		}
		return tMap.remove(t.longId(), t);
	}

	@Override
	public void clear() {
		if(logger.isDebugEnabled()) {
			logger.debug("clear "+getClass().getSimpleName());
		}
		tMap.clear();
	}
	
	
}
