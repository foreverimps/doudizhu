package com.zj.platform.gamecenter.events;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zj.platform.gamecenter.eventHandlers.EventHandler;
import com.zj.platform.gamecenter.eventHandlers.EventHandlerManager;
import com.zj.platform.gamecenter.utils.ThreadPoolUtil;

/**
 * 用户事件异步发布器 <br>
 * 事件的处理无先后顺序
 * <p>
 * Copyright: Copyright (c) 2014年12月29日 下午9:53:14
 * <p>
 * Company:
 * <p>
 * 
 * @author H.L.Wan
 * @version 1.0.0
 */
@Component
public class AyncUserEventPublisher {

	@Autowired
	private EventHandlerManager handlerManager;

	/**
	 * 发布用户事件，异步调用已加载的处理器处理事件
	 * 
	 * @param userEvent
	 */
	public void publishUserEvent(final UserEvent userEvent) {
		List<EventHandler> handlers = handlerManager.getHandlersByEventCode(userEvent.getEventCode());
		if (!handlers.isEmpty()) {
			for (int i = 0; i < handlers.size(); i++) {
				final EventHandler eventHandler = handlers.get(i);
				ThreadPoolUtil.run(new Runnable() {

					@Override
					public void run() {
						eventHandler.handleEvent(userEvent);
					}

				});
			}
		}
	}
}
