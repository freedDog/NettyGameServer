package com.game.db.service.redis;

/**
 * 列表类型的缓存对象
 * @author JiangBangMing
 *
 * 2018年6月20日 下午1:02:27
 */
public interface RedisListInterface {

	public String getShardingKey();
	
	public String getRedisKeyEnumString();
	
	/**
	 * 列表对象的子唯一主键属性
	 * @return
	 */
	public String getSubUniqueKey();
}
