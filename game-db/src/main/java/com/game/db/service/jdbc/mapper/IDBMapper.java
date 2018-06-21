package com.game.db.service.jdbc.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.game.db.entity.IEntity;

/**
 * 基础mapper
 * @author JiangBangMing
 *
 * 2018年6月21日 上午11:50:23
 */
public interface IDBMapper<T extends IEntity>{

	public long insertEntity(T entity);
	
	public IEntity getEntity(T entity);
	
	public List<T> getEntityList(T entity);
	
	public List<T> getEntityList(T entity,RowBounds rowBounds);
	
	/**
	 * 直接查找db,无缓存
	 * @param map
	 * @return
	 */
	public List<T> filterList(Map map);
	
	/**
	 * 直接查找db,无缓存
	 * @param map
	 * @param rowBounds
	 * @return
	 */
	public List<T> filterList(Map map,RowBounds rowBounds);
	
	public void updateEntity(Map map);
	public void deleteEntity(T entity);
}
