package com.zj.platform.gamecenter.eventHandlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class EventHandlerManager {

	private final Map<Integer, List<EventHandler>> handlerMap = new HashMap<Integer, List<EventHandler>>();

	public List<EventHandler> getHandlersByEventCode(int code) {
		return handlerMap.get(code);
	}

	public void registerHandler(int code, EventHandler handler) {
		if (null == handler) {
			return;
		}
		List<EventHandler> handlers = handlerMap.get(code);
		if (handlers == null) {
			handlers = new ArrayList<EventHandler>();
		}
		handlers.add(handler);
	}

	public void registerHandler(int code, Class<? extends EventHandler> clazz) {
		EventHandler eventHandler = null;
		try {
			eventHandler = clazz.newInstance();
		}
		catch (Exception e) {
			// 日志
		}
		registerHandler(code, eventHandler);
	}

	@SuppressWarnings("unchecked")
	public void registerHandler(int code, String clazzFullName) {
		ClassLoader cl = this.getClass().getClassLoader();
		try {
			Class<EventHandler> clazz = (Class<EventHandler>) cl.loadClass(clazzFullName);
			registerHandler(code, clazz);
		}
		catch (ClassNotFoundException e) {
			// 日志
		}

	}

}
