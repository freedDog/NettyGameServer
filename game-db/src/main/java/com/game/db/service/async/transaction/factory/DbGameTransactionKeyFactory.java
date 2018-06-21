package com.game.db.service.async.transaction.factory;

import org.springframework.stereotype.Service;

import com.redis.config.GlobalConstants;
import com.redis.transaction.enums.GameTransactionEntityCause;
import com.redis.transaction.factory.GameTransactionKeyFactory;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月21日 下午4:41:35
 */
@Service
public class DbGameTransactionKeyFactory extends GameTransactionKeyFactory{

	/**
	 * 获取玩家锁
	 * @param cause
	 * @param redisKey
	 * @param union
	 * @return
	 */
	public String getPlayerTransactionEntityKey(GameTransactionEntityCause cause,String redisKey,String union) {
		return redisKey+cause.getCause()+GlobalConstants.Strings.commonSplitString+union;
	}
}
