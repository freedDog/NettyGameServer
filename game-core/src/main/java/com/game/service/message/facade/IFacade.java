package com.game.service.message.facade;
/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月1日 下午12:18:29
 */

import com.game.common.exception.GameHandlerException;
import com.game.service.message.AbstractNetMessage;

public interface IFacade {
	public AbstractNetMessage dispatch(AbstractNetMessage message) throws GameHandlerException;
}
