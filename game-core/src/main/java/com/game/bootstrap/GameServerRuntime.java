package com.game.bootstrap;
/**
 * 游戏服务器运行是数据
 * @author JiangBangMing
 *
 * 2018年6月1日 下午4:26:16
 */
public class GameServerRuntime {
	/**服务是否已经开放*/
	private static volatile boolean open;
	/**服务是否正在关闭*/
	private static volatile boolean shutdowning;
	/**是否进行些流量控制，默认是 true,即进行写流量控制*/
	private static volatile boolean writeTrafficControl=true;
	/**是否进行读流量控制，默认是 true，即进行读流量控制*/
	private static volatile boolean readTrafficControl=true;
	/**玩家的session 里写队列的最大字节数，超出此值时将连接断掉*/
	public static final int MAX_WRITE_BYTES_INQUEUE=300*1024;
	
	private GameServerRuntime() {
		
	}
	/**
	 * 检查服务是否已经开放，只有在已经开放的情况下才能接收玩家的连接
	 * @return
	 */
	public static boolean isOpen() {
		return open;
	}
	/**
	 * 打开服务
	 */
	public static void setOpen() {
		open=true;
	}
	/**
	 * 关闭服务
	 */
	public static void setOpenOff() {
		open=false;
	}
	/**
	 * 设置服务器正在关闭
	 */
	public static void setShutdowning() {
		shutdowning=true;
		setOpenOff();
	}
	/**
	 * 服务器是否正在关闭
	 * @return
	 */
	public static boolean isShutdowning() {
		return shutdowning;
	}
	/**
	 * 是否对玩家session 进行写流量控制
	 * @return
	 */
	public static boolean isWriteTrafficControl() {
		return writeTrafficControl;
	}
	/**
	 * 设置是否对玩家进行写流量控制
	 * @param writeTrafficControl
	 */
	public static void setWriteTrafficControl(boolean writeTrafficControl) {
		GameServerRuntime.writeTrafficControl = writeTrafficControl;
	}
	/**
	 * 是否对玩家session进行读流量的控制
	 * @return
	 */
	public static boolean isReadTrafficControl() {
		return readTrafficControl;
	}
	/**
	 * 设置是否对玩家进行读流量的控制
	 * @param readTrafficControl
	 */
	public static void setReadTrafficControl(boolean readTrafficControl) {
		GameServerRuntime.readTrafficControl = readTrafficControl;
	}
	
}
