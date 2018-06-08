package com.game.executor.update.entity;

import java.io.Serializable;

/**
 * 基本的抽象
 * @author JiangBangMing
 *
 * 2018年6月6日 上午11:39:19
 */
public abstract class AbstractUpdate <ID extends Serializable> implements IUpdate<ID>{
	private static final long serialVersionUID = 1L;
	//是否存放标志
    private boolean activeFlag = true;
    //标示id
    private ID updateId;

    @Override
    public boolean isActive() {
        return activeFlag;
    }

    @Override
    public void setActive(boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    @Override
    public ID getUpdateId() {
        return updateId;
    }

    public void setUpdateId(ID updateId) {
        this.updateId = updateId;
    }
}
