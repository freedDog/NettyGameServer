package com.game.bootstrap.manager;
/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月1日 下午3:43:34
 */
public interface ILocalManager {
	
	public <T> T get(Class<T> clazz);
	
	public <X,Y extends X> void create(Class<Y> clazz,Class<X> inter) throws Exception;
	
	public <T> void add(Object service,Class<T> inter);
	
	public void shutdown();
}
