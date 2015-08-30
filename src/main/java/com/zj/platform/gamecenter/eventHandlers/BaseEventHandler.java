package com.zj.platform.gamecenter.eventHandlers;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseEventHandler implements EventHandler, InitializingBean {

	@Autowired
	protected EventHandlerManager manager;

	@Override
	public void afterPropertiesSet() throws Exception {
		manager.registerHandler(eventCode(), this);
	}

}
