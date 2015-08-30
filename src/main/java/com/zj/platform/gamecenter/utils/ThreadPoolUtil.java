package com.zj.platform.gamecenter.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具类 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2014年12月29日 下午9:54:08
 * <p>
 * Company:
 * <p>
 * 
 * @author
 * @version 1.0.0
 */
public class ThreadPoolUtil {

	private static ExecutorService pools = Executors.newFixedThreadPool(100);

	private static ScheduledExecutorService sPools = Executors.newScheduledThreadPool(100);

	public static void run(Runnable command, long delay, TimeUnit unit) {
		sPools.schedule(command, delay, unit);
	}

	public static void run(Runnable command) {
		if (null != command) {
			pools.execute(command);
		}
	}

	public static int getThreadCount() {
		return Thread.activeCount();
	}

	public static Thread getCurrent() {
		return Thread.currentThread();
	}
}
