package com.game.db.service.entity;

import java.util.List;

import com.game.db.entity.AbstractEntity;
import com.game.db.entity.IEntity;

/**
 * 基础服务
 * @author JiangBangMing
 *
 * 2018年6月20日 下午9:42:00
 */
public interface IEntityService<T extends AbstractEntity> {
	
	/**
	 * 插入实体
	 * @param entity
	 * @return
	 */
	public long insertEntity(T entity);
	
	/**
	 * 查询实体
	 * @param entity
	 * @return
	 */
	public IEntity getEntity(T entity);
	
	/**
	 * 查询实体列表
	 * @param entity 需要实现代理，才能拼写sql map
	 * @return
	 */
	public List<T> getEntityList(T entity);
	
	/**
	 * 更新实体
	 * @param entity 需要实现代理
	 */
	public void updateEntity(T entity);
	
	/**
	 * 删除实体
	 * @param entity
	 */
	public void deleteEntity(T entity);
	
	/**
	 * 批量出入实体列表
	 * @param entityList
	 * @return
	 */
	public List<Long> insertEntityBatch(List<T> entityList);
	
    /**
     * 批量更新实体列表
     * @param entityList
     */
    public void updateEntityBatch(List<T> entityList);

	
	/**
	 * 批量删除实体列表
	 * @param entityList
	 */
	public void deleteEntityBatch(List<T> entityList);
	
	/**
	 * 获取sharding 后的结果
	 * @param entity
	 * @return
	 */
	public long getShardingId(T entity);
}
