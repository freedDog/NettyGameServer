package com.game.service.uuid;

import java.util.concurrent.atomic.AtomicLong;

/**
 * session Id 生成器
 * @author JiangBangMing
 *
 * 2018年6月6日 上午11:51:32
 */
public class LongIdGenerator {

	protected AtomicLong id_gen=new AtomicLong(0);
	
	public long generateId() {
		return id_gen.incrementAndGet();
	}
}
