package com.game.executor.update.cache;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.game.executor.event.impl.event.UpdateEvent;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月11日 下午12:53:45
 */
public class UpdateEventPoolFactory implements PooledObjectFactory<UpdateEvent>{
	@Override
	public void destroyObject(PooledObject<UpdateEvent> p) throws Exception {
		UpdateEvent updateEvent=p.getObject();
		updateEvent=null;
	}

	@Override
	public PooledObject<UpdateEvent> makeObject() throws Exception {
		UpdateEvent updateEvent=new UpdateEvent();
		return new DefaultPooledObject(updateEvent);
	}

	@Override
	public void activateObject(PooledObject<UpdateEvent> arg0) throws Exception {
		
	}

	@Override
	public void passivateObject(PooledObject<UpdateEvent> arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean validateObject(PooledObject<UpdateEvent> arg0) {
		return false;
	}

}
