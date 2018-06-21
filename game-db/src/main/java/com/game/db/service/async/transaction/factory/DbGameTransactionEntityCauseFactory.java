package com.game.db.service.async.transaction.factory;

import org.springframework.stereotype.Service;

import com.redis.transaction.enums.GameTransactionEntityCause;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月21日 下午8:01:58
 */
@Service
public class DbGameTransactionEntityCauseFactory {

	public final GameTransactionEntityCause asyncDbSave=new GameTransactionEntityCause("asyncDbSave");

	public GameTransactionEntityCause getAsyncDbSave() {
		return asyncDbSave;
	}
	
	
}
