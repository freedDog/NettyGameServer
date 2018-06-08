package com.game.service.net.tcp.session;

import com.game.executor.update.entity.AbstractUpdate;

/**
 * 网络检查更新
 * @author JiangBangMing
 *
 * 2018年6月6日 上午11:34:55
 */
public class TcpNetStateUpdate extends AbstractUpdate {
	
	public volatile TcpNetState state=TcpNetState.CONNECTED;
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	public void updateConnect() {
		if(state==TcpNetState.DISCONNECTING) {
			setDisconnected();
			return;
		}else if(state==TcpNetState.DISCONNECTED) {
			processDisconnect();
			return;
		}
	}
	
	public void processDisconnect() {
		state=TcpNetState.DESTROY;
	}
	
	public void setDisconnected() {
		state=TcpNetState.DISCONNECTED;
	}
	public void setDisconnecting() {
		state=TcpNetState.DISCONNECTING;
	}
}
