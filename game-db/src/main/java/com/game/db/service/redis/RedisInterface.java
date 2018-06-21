package com.game.db.service.redis;

/**
 * 默认保存为map,保证更新不会被覆盖
 * @author JiangBangMing
 *
 * 2018年6月20日 下午1:00:58
 */
public interface RedisInterface {

	public String getUnionKey();
	
	public String getRedisKeyEnumString();
}
