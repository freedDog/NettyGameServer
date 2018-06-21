package com.game.db.entity;

import com.game.db.common.annotation.EntitySave;
import com.game.db.common.annotation.FieldSave;
import com.game.db.common.annotation.MethodSaveProxy;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月21日 上午11:25:06
 */
@EntitySave
public class BaseLongIDEntity extends AbstractEntity<Long>{

	@FieldSave
	private Long id;
	
	public Long getId() {
		return id;
	}

	@Override
	@MethodSaveProxy(proxy="id")
	public void setId(Long id) {
		this.id=id;
	}

}
