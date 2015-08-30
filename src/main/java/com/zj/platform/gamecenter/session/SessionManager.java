package com.zj.platform.gamecenter.session;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class SessionManager {

	private final Map<String, SocketSession> sessions = new HashMap<String, SocketSession>();// sessionId,session

	private final Map<String, Long> sessionIdToUserId = new HashMap<String, Long>();// sessionId,UserId

	private final LinkedList<Long> userIds = new LinkedList<Long>();

	private final Lock lock = new ReentrantLock();// 锁对象

	/**
	 * 添加SocketSession
	 * 
	 * @param userId
	 * @param session
	 */
	public void addUserSession(Long userId, SocketSession session) {
		lock.lock();
		try {
			if (userIds.contains(userId)) {// 表示用户之前进入游戏
				if (session.getId().equals(sessionIdToUserId.get(userId))) {// 两个session相等

				} else {
					addSession(session);
					sessionIdToUserId.put(session.getId(), userId);
				}
			} else {
				userIds.add(userId);
				sessionIdToUserId.put(session.getId(), userId);
			}
		}
		finally {
			lock.unlock();
		}
	}

	/**
	 * 添加SocketSession
	 * 
	 * @param userId
	 * @param session
	 */
	public void addSession(SocketSession session) {
		lock.lock();
		try {
			sessions.put(session.getId(), session);
		}
		finally {
			lock.unlock();
		}
	}

	public Long removeSessionBySessionId(String sessionId) {
		lock.lock();
		try {
			Long userId = sessionIdToUserId.remove(sessionId);
			if (null != userId) {
				sessions.remove(userId);
				userIds.remove(userId);
			}
			return userId;
		}
		finally {
			lock.unlock();
		}
	}

	public void removeSessionByUserId(Long userId) {
		lock.lock();
		try {
			userIds.remove(userId);
			SocketSession old = sessions.remove(userId);
			if (old != null) {
				sessionIdToUserId.remove(old.getId());
			}
		}
		finally {
			lock.unlock();
		}
	}

	public void addSession(Long userId, IoSession ioSession) {
		if (null != ioSession && null != userId) {
			addUserSession(userId, new MinaSocketSessionWrap(ioSession));
		}
	}

	public void addSession(Long userId, WebSocketSession socketSession) {
		if (null != socketSession && null != userId) {
			addUserSession(userId, new WebSocketSessionWrap(socketSession));
		}
	}

	public Long removeSession(WebSocketSession socketSession) {
		return removeSessionBySessionId(WebSocketSessionWrap.ID_PREFIX + socketSession.getId());
	}

	public Long removeSession(IoSession session) {
		return removeSessionBySessionId(MinaSocketSessionWrap.ID_PREFIX + String.valueOf(session.getId()));
	}

	public void removeSession(Long userId) {
		removeSessionByUserId(userId);
	}

	public List<SocketSession> getSessions() {
		lock.lock();
		try {
			LinkedList<SocketSession> socketSessions = new LinkedList<SocketSession>(sessions.values());
			return socketSessions;
		}
		finally {
			lock.unlock();
		}
	}

	public Map<Long, SocketSession> getSessionMap() {
		Map<Long, SocketSession> map = new LinkedHashMap<Long, SocketSession>();
		lock.lock();
		try {
			// for (Long id : sessions.keySet()) {
			// map.put(id, sessions.get(id));
			// }
		}
		finally {
			lock.unlock();
		}
		return map;
	}

	public List<Long> getUserIds() {
		lock.lock();
		try {
			List<Long> ids = new LinkedList<Long>(userIds);
			return ids;
		}
		finally {
			lock.unlock();
		}
	}

	public Set<String> getSessionIds() {
		lock.lock();
		try {
			Set<String> sessionIds = new HashSet<String>(sessionIdToUserId.keySet());
			return sessionIds;
		}
		finally {
			lock.unlock();
		}
	}

}
