package com.game.db.entity;

import com.game.db.common.annotation.EntitySave;
import com.game.db.common.annotation.FieldSave;
import com.game.db.common.annotation.MethodSaveProxy;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月21日 上午11:26:27
 */
@EntitySave
public class BaseStringIDEntity extends AbstractEntity<String>{

	@FieldSave
	private String id;

	@Override
	public String getId() {
		return id;
	}

	@Override
	@MethodSaveProxy(proxy="id")
	public void setId(String id) {
		this.id=id;
	}
	
}
