package com.game.service.dict;

import java.util.Collection;

/**
 * 数据字典集合
 * @author JiangBangMing
 *
 * 2018年6月6日 下午12:57:35
 */
public interface IDictCollections {

	/**
	 * 获取所有数据字典
	 * @return
	 */
	public Collection<IDict> getAllDicts();
}
