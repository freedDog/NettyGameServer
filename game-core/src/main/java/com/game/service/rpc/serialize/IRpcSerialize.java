package com.game.service.rpc.serialize;
/**
 * rpc对象序列化
 * @author JiangBangMing
 *
 * 2018年6月5日 下午2:08:11
 */
public interface IRpcSerialize {

	public <T> byte[] serialize(T obj);
	
	public <T> T deserialize(byte[] data,Class<T> cls);
}
