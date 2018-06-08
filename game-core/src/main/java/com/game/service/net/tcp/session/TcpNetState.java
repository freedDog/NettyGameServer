package com.game.service.net.tcp.session;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月6日 上午11:33:52
 */
public enum TcpNetState {
    /**链接状态*/
    CONNECTED,
    /**掉线中*/
    DISCONNECTING,
    /**掉线*/
    DISCONNECTED,
    /**销毁*/
    DESTROY;
	
}
