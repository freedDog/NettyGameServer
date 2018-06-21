package com.game.db.entity;

import java.io.Serializable;
import java.util.Date;

import com.game.db.common.annotation.EntitySave;
import com.game.db.common.annotation.FieldSave;
import com.game.db.common.annotation.MethodSaveProxy;
import com.game.db.service.entity.EntityKeyShardingStrategyEnum;
import com.game.db.service.proxy.EntityProxyWrapper;
import com.game.db.sharding.ShardingTable;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月20日 上午11:57:55
 */
@EntitySave
public abstract class AbstractEntity<ID extends Serializable> extends ShardingTable implements ISoftDeleteEntity<ID>{

	@FieldSave
	private boolean deleted;
	@FieldSave
	private Date deleteTime;
	
	@FieldSave
	private long userId;
	
	//用于记录数据库封装对象
	private EntityProxyWrapper entityProxyWrapper;

	public EntityProxyWrapper getEntityProxyWrapper() {
		return entityProxyWrapper;
	}

	public void setEntityProxyWrapper(EntityProxyWrapper entityProxyWrapper) {
		this.entityProxyWrapper = entityProxyWrapper;
	}

	public boolean isDeleted() {
		return deleted;
	}
	@MethodSaveProxy(proxy="deleted")
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}
	@MethodSaveProxy(proxy="deleteTime")
	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public long getUserId() {
		return userId;
	}
	@MethodSaveProxy(proxy="userId")
	public void setUserId(long userId) {
		this.userId = userId;
	}
    public EntityKeyShardingStrategyEnum getEntityKeyShardingStrategyEnum(){
        return EntityKeyShardingStrategyEnum.USER_ID;
    }
	
}
