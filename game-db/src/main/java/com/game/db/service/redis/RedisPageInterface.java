package com.game.db.service.redis;

import java.io.Serializable;

/**
 * redis 分页数据接口
 * 分页接口需要额外填写 zset 需要提供key,score
 * @author JiangBangMing
 *
 * 2018年6月20日 下午5:57:23
 */
public interface RedisPageInterface {

	public RedisKeyEnum getPageRedisKeyEnum();
	
	public Serializable getScore();
}
