package com.game.service.lookup;

public interface ILongLookupService<T extends ILongId> {
	/**
	 * 查找
	 * @param id
	 * @return
	 */

	public T lookup(long id);
	/**
	 * 增加
	 * @param t
	 */
	public void addT(T t);
	/**
	 * 移除
	 * @param t
	 * @return
	 */
	public boolean removeT(T t);
	/**
	 * 清除所有
	 */
	public void clear();
}
