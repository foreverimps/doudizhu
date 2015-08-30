package com.zj.platform.gamecenter.session;

import java.io.IOException;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocketSessionWrap<br>
 * 封装 WebSocketSession 以做统一处理
 * <p>
 * Copyright: Copyright (c) 2014年12月31日 上午12:19:53
 * <p>
 * Company:
 * <p>
 * 
 * @author H.L.Wan
 * @version 1.0.0
 */
public class WebSocketSessionWrap implements SocketSession {

	public static final String ID_PREFIX = "_WebSocketSession";

	private final WebSocketSession session;

	public WebSocketSessionWrap(WebSocketSession session) {
		this.session = session;
	}

	@Override
	public String getId() {
		return ID_PREFIX + session.getId();
	}

	@Override
	public boolean isClose() {
		return !session.isOpen();
	}

	@Override
	public boolean isOpen() {
		return session.isOpen();
	}

	@Override
	public void sendMessage(String message) {
		try {
			session.sendMessage(new TextMessage(message));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof WebSocketSessionWrap)) {
			return false;
		}
		WebSocketSessionWrap wrap = (WebSocketSessionWrap) obj;
		if (null == wrap.getId()) {
			return false;
		}
		return wrap.getId().equals(getId());
	}

}
