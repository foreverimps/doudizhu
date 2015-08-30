package com.zj.platform.gamecenter.session;

import org.apache.mina.core.session.IoSession;

/**
 * MinaSocketSessionWrap <br>
 * 封装MinaSocketSession,以做统一处理
 * <p>
 * Copyright: Copyright (c) 2014年12月31日 上午12:21:01
 * <p>
 * Company:
 * <p>
 * 
 * @author H.L.Wan
 * @version 1.0.0
 */
public class MinaSocketSessionWrap implements SocketSession {

	public static final String ID_PREFIX = "_MinaSocketSession";

	private final IoSession session;

	public MinaSocketSessionWrap(IoSession session) {
		this.session = session;
	}

	@Override
	public String getId() {
		return ID_PREFIX + String.valueOf(session.getId());
	}

	@Override
	public boolean isClose() {
		return !session.isConnected();
	}

	@Override
	public boolean isOpen() {
		return session.isConnected();
	}

	@Override
	public void sendMessage(String message) {
		session.write(message);
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
		if (!(obj instanceof MinaSocketSessionWrap)) {
			return false;
		}
		MinaSocketSessionWrap wrap = (MinaSocketSessionWrap) obj;
		if (null == wrap.getId()) {
			return false;
		}
		return wrap.getId().equals(getId());
	}

}
