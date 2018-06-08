package com.game.executor.common;

/**
 * 更新器质性类型
 * @author JiangBangMing
 *
 * 2018年6月7日 上午12:58:33
 */
public enum UpdateExecutorEnum {
    /*使用locksupport方式*/
    locksupport,
    /*使用绑定线程*/
    bindThread,
    /**使用disruptor*/
    disruptor,
    ;
}
