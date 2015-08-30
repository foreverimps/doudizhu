package com.zj.platform.gamecenter.session;

/**
 * 统一封装WebSocketSession 与IoSession <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2014年12月31日 上午12:05:03
 * <p>
 * Company:
 * <p>
 * 
 * @author H.L.Wan
 * @version 1.0.0
 */
public interface SocketSession {

	/**
	 * 获取SocketSessionId
	 * 
	 * @return
	 */
	public String getId();

	/**
	 * 判断SocketSession是否已关闭
	 * 
	 * @return
	 */
	public boolean isClose();

	/**
	 * 判断SocketSession是否打开
	 * 
	 * @return
	 */
	public boolean isOpen();

	/**
	 * 发送文本消息
	 * 
	 * @param message
	 */
	public void sendMessage(String message);

}
