package com.zj.platform.gamecenter.events;

import com.zj.platform.gamecenter.constant.EventTypeEnum;

/**
 * 系统事件 <br>
 * 在平台上触发的事件，不限于某个游戏
 * <p>
 * Copyright: Copyright (c) 2014年12月29日 下午8:37:47
 * <p>
 * Company:
 * <p>
 * 
 * @author H.L.Wan
 * @version 1.0.0
 */
public class SystemEvent extends UserEvent {

	private final EventTypeEnum.SystemEventTypeEnum eventType;

	public SystemEvent(Long userId, EventTypeEnum.SystemEventTypeEnum eventType) {
		super(userId);
		this.eventType = eventType;
	}

	@Override
	public int getEventCode() {
		if (null != eventType) {
			return eventType.getCode();
		}
		return -1;
	}

}
