package com.game.db.service.async.transaction.factory;

import org.springframework.stereotype.Service;

import com.redis.transaction.enums.GameTransactionCause;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月21日 下午8:03:19
 */
@Service
public class DbGameTransactionCauseFactory {
	
	public final GameTransactionCause asyncDbSave=new GameTransactionCause("asyncDbSave");

	public GameTransactionCause getAsyncDbSave() {
		return asyncDbSave;
	}
}
