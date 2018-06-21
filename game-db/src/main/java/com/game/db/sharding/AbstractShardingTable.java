package com.game.db.sharding;

/**
 * 抽象的分片表
 * @author JiangBangMing
 *
 * 2018年6月20日 上午11:59:36
 */
public abstract class AbstractShardingTable {

	private  Integer sharding_table_index;

	public Integer getSharding_table_index() {
		return sharding_table_index;
	}

	public void setSharding_table_index(Integer sharding_table_index) {
		this.sharding_table_index = sharding_table_index;
	}
	
	
}
