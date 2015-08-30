package com.zj.platform.gamecenter.service.socket;

import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;

@Service
public class HandlerService {

	private final Lock lock = new ReentrantLock();// 锁对象

	/**
	 * 是否已握手
	 * 
	 * @return true:是，false：否
	 */
	public boolean hasHands(String sessionId, Set<String> sessionIds) {
		lock.lock();
		try {
			return sessionIds.contains(sessionId);
		}
		finally {
			lock.unlock();
		}
	}

}
