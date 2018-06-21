package com.game.db.entity;

import java.io.Serializable;

/**
 * 基本的数据存储对象
 * @author JiangBangMing
 *
 * 2018年6月20日 上午11:54:04
 */
public interface IEntity <ID extends Serializable> extends Serializable {

	public ID getId();
	
	public void setId(ID id);
}
