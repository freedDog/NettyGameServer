package com.game.common.constant;

import java.util.concurrent.TimeUnit;

/**
 * 全局变量
 * @author JiangBangMing
 *
 * 2018年6月1日 下午12:10:38
 */
public class GlobalConstants {
	/**
	 * 默认变量
	 * @author JiangBangMing
	 *
	 * 2018年6月1日 下午12:11:57
	 */
	public static class Constants{
		public static final int session_prcoss_message_max_size=10;
		public static final int life_cycle_interval=6000;
	}
    /**
     *上传协议
     */
    public static class FileExtendConstants {
        public static final String Ext = ".class";
    }
    /**
     * Thread的名字前缀
     */
    public static class Thread{
        public static final String NET_TCP_BOSS ="net_tcp_boss";
        public static final String NET_TCP_WORKER ="net_tcp_worker";
        public static final String GAME_MESSAGE_QUEUE_EXCUTE="game_message_queue";
        public static final String NET_UDP_WORKER ="net_udp_worker";
        public static final String NET_UDP_MESSAGE_PROCESS ="net_udp_message_process";
        public static final String NET_RPC_BOSS ="net_rpc_boss";
        public static final String NET_RPC_WORKER ="net_rpc_worker";
        public static final String START_FINISHED ="start_finished";
        public static final String DETECT_RPCPENDING ="detect_rpcpending";
        public static final String GAME_ASYNC_CALL = "game_async_call";
        public static final String RPC_HANDLER = "rpc_handler";
        public static final String ASYNC_EVENT_WORKER = "async_event_worker";
        public static final String ASYNC_EVENT_HANDLER = "async_event_handler";
        public static final String UPDATE_EXECUTOR_SERVICE = "UpdateExecutorService";
        public static final String NET_PROXY_BOSS ="net_proxy_boss";
        public static final String NET_PROXY_WORKER ="net_proxy_worker";
        public static final String NET_HTTP_BOSS ="net_http_boss";
        public static final String NET_HTTP_WORKER ="net_http_worker";

        public static final String NET_WEB_SOCKET_BOSS ="net_web_socket_boss";
        public static final String NET_WEB_SOCKET_WORKER ="net_web_socket_worker";
    }
    /**
     * redis Key的基本配置
     */
    public static class ConfigFile{
        /**game server*/
        public static final String GAME_SERVER_CONIFG="game_server.cfg.js";
        /**game server*/
        public static final String GAME_SERVER_DIFF_CONIFG="game_server_diff.cfg.js";
        /**rpc*/
        public static final String RPC_SERVER_REGISTER_CONFIG ="rpc-server-register.xml";
        /**dynmic*/
        public static final String DYNAMIC_CONFIG = "dynamic_config.properties";
        /**zookeeper*/
        public static final String ZOOKEEPER_CONFIG = "zookeeper.properties";
        /**rpcservice*/
        public static final String RPC_SERVEICE_CONFIG="rpc-service-register.xml";
        /**dictService*/
        public static final String DICT_ROOT_FILE="dict/dict.wg";
        /**proxy*/
        public static final String PROXY_SERVER_CONFIG="proxy-server.xml";
        /**udp-server*/
        public static final String UDP_SERVER_CONFIG="udp-server.xml";
        /**http-server*/
        public static final String HTTP_SERVER_CONFIG="http-server.xml";
        /**websocket-server*/
        public static final String WEBSOCKET_SERVER_CONFIG="websocket-server.xml";
    }
    /**
     * JSONFile
     */
    public static class JSONFile{
        public static final String  dict_package  = "package";
        public static final String  dict_fils  = "dict";
        public static final String  multiKey  = "multiKey";
        public static final String  body  = "body";

    }
    public static class WheelTimer{
        public static final int tickDuration = 20;
        public static final TimeUnit timeUnit = TimeUnit.SECONDS;
        public static final int ticksPerWheel = 60;
    }
}
