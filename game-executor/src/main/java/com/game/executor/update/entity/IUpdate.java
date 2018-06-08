package com.game.executor.update.entity;

import java.io.Serializable;

/**
 * 基础循环接口
 * @author JiangBangMing
 *
 * 2018年6月6日 上午11:37:54
 */
public interface IUpdate <ID extends Serializable> extends Serializable{

    public void update();
    public ID getUpdateId();
    public boolean isActive();
    public void setActive(boolean activeFlag);
}
